package me.changwook.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.common.ApiResponse;
import me.changwook.member.dto.AuthResponseDTO;
import me.changwook.member.dto.LoginRequestDTO;
import me.changwook.member.dto.MemberDTO;
import me.changwook.config.security.JwtUtil;
import me.changwook.member.auth.RefreshToken;
import me.changwook.member.auth.RefreshTokenRepository;
import me.changwook.alert.NotificationService;
import me.changwook.member.auth.LoginService;
import me.changwook.common.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginService loginService;
    private final MemberService memberService;
    private final NotificationService notificationService;
    private final ResponseFactory responseFactory;


    /**
     * @param loginRequestDTO 로그인을 할때 입력하는 이메일과 비밀번호를 담아오는 객체
     * @param response        HTTP 응답 Header에 쿠키 항목을 추가하기 위한 변수
     * @return ResponseEntity<ApiResponse < AuthResponseDTO>>
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        Map<String, Object> loginResult = loginService.login(loginRequestDTO);
        
        AuthResponseDTO authResponse = (AuthResponseDTO) loginResult.get("authResponse");
        String refreshToken = (String) loginResult.get("refresh-token");

        // RefreshToken 쿠키 설정
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(jwtUtil.getRefreshInterval() / 1000)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", refreshCookie.toString());

        // AccessToken도 쿠키에 저장 (페이지 네비게이션 시 자동 인증을 위해)
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", authResponse.getToken())
                .httpOnly(false)  // JavaScript에서 접근 가능하도록
                .path("/")
                .maxAge(jwtUtil.getExpiration() / 1000)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", accessCookie.toString());

        return responseFactory.success("로그인이 성공했습니다.", authResponse);
    }

    //    AJAX방식으로 클라이언트 단계에서 토큰을 재발급하는 로직(서버계층으로 바꿈)
    @Deprecated
    @PostMapping("/refresh-token")
    @Transactional
    public ResponseEntity<ApiResponse<AuthResponseDTO>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String oldRefreshToken = extractRefreshToken(request, "refreshToken");

        if (oldRefreshToken == null) {
            return responseFactory.error("Refresh Token이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        // 토큰 유효성 검증 및 사용자 이름 추출
        String username = jwtUtil.validateToken(oldRefreshToken) ? jwtUtil.getUsernameFromToken(oldRefreshToken) : null;
        String role = jwtUtil.validateToken(oldRefreshToken) ? jwtUtil.getRoleFromToken(oldRefreshToken) : null;

        if (username == null || role == null) {
            return responseFactory.error("유효하지 않는 Refresh Token 입니다.", HttpStatus.UNAUTHORIZED);
        }

        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByUsername(username);

        // 탈취 감지 로직
        if (tokenOpt.isEmpty() || !tokenOpt.get().getToken().equals(oldRefreshToken)) {
            log.error("CRITICAL: Refresh Token 탈취 의심이 됩니다!! 사용자:{}, IP:{}", username, getClientIp(request));

            refreshTokenRepository.deleteByUsername(username);
            notificationService.notifyAdminOfTokenTheft(username, getClientIp(request));

            return responseFactory.error("비정상적인 접근이 감지되었습니다.", HttpStatus.UNAUTHORIZED);
        }

        // 토큰 순환: 새로운 토큰들을 발급
        String newAccessToken = jwtUtil.generateAccessToken(username, role);
        String newRefreshToken = jwtUtil.generateRefreshToken(username, role);

        // DB 새로운 Refresh Token저장 (기존 토큰 업데이트)
        RefreshToken savedToken = tokenOpt.get();
        savedToken.setToken(newRefreshToken);
        savedToken.setExpiryDate(jwtUtil.getExpirationDateFromToken(newRefreshToken).getTime());
        refreshTokenRepository.save(savedToken);

        // RefreshToken 쿠키 업데이트
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(jwtUtil.getRefreshInterval() / 1000)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", refreshCookie.toString());

        // AccessToken 쿠키도 업데이트
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(false)
                .path("/")
                .maxAge(jwtUtil.getExpiration() / 1000)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", accessCookie.toString());

        // 사용자 정보를 다시 조회하여 최신 정보 반환
        MemberDTO memberInfo = memberService.findByEmail(username);
        
        AuthResponseDTO authResponseDTO = AuthResponseDTO.builder()
                .token(newAccessToken)
                .email(memberInfo.getEmail())
                .name(memberInfo.getName())
                .role(Role.valueOf(role.replace("ROLE_", "")))
                .build();

        return responseFactory.success("토큰이 성공적으로 갱신되었습니다.", authResponseDTO);
    }


    private String extractRefreshToken(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

        @PostMapping("/logout")
        public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
            String refreshToken = extractRefreshToken(request, "refreshToken");
            if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
                String username = jwtUtil.getUsernameFromToken(refreshToken);
                refreshTokenRepository.deleteByUsername(username);
            }
            // RefreshToken 쿠키 삭제
            ResponseCookie deleteRefreshCookie = ResponseCookie.from("refreshToken", "")
                    .path("/")
                    .maxAge(0)
                    .build();
            response.addHeader("Set-Cookie", deleteRefreshCookie.toString());
            
            // AccessToken 쿠키도 삭제
            ResponseCookie deleteAccessCookie = ResponseCookie.from("accessToken", "")
                    .path("/")
                    .maxAge(0)
                    .build();
            response.addHeader("Set-Cookie", deleteAccessCookie.toString());

            return responseFactory.success("로그아웃 되었습니다.");
        }
    }
