package me.changwook.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.DTO.AuthResponseDTO;
import me.changwook.DTO.LoginRequestDTO;
import me.changwook.configuration.config.security.JwtUtil;
import me.changwook.domain.RefreshToken;
import me.changwook.repository.RefreshTokenRepository;
import me.changwook.service.NotificationService;
import me.changwook.service.impl.LoginService;
import me.changwook.service.impl.MemberService;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginService loginService;
    private final MemberService memberService;
    private final NotificationService notificationService;


    /**
     * @param loginRequestDTO 로그인을 할때 입력하는 이메일과 비밀번호를 담아오는 객체
     * @param response        HTTP 응답 Header에 쿠키 항목을 추가하기 위한 변수
     * @return ResponseEntity<ApiResponseDTO < AuthResponseDTO>>
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        Map<String, String> token = loginService.login(loginRequestDTO);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", token.get("refresh-token"))
                .httpOnly(true)
                .path("/")
                .maxAge(jwtUtil.getRefreshInterval() / 1000)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        AuthResponseDTO accessToken = new AuthResponseDTO(token.get("access_token"));

        ApiResponseDTO<AuthResponseDTO> responseDTO = new ApiResponseDTO<>(true, "로그인이 성공했습니다.", accessToken);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/refresh-token")
    @Transactional
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String oldRefreshToken = extractRefreshToken(request, "refreshToken");

        if (oldRefreshToken == null) {
            return ResponseEntity.status(401).body(new ApiResponseDTO<>(false, "Refresh Token이 존재하지 않습니다.", null));
        }

        //토큰 유효성 검증 및 사용자 이름 추출
        String username = jwtUtil.validateToken(oldRefreshToken) ? jwtUtil.getUsernameFromToken(oldRefreshToken) : null;

        if (username == null) {
            return ResponseEntity.status(401).body(new ApiResponseDTO<>(false, "유효하지 않는 Refresh Token 입니다.", null));
        }

        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByUsername(username);

        //탈취 감지 로직
        //DB에 저장된 토큰이 없거나, 요청으로 들어온 토큰과 일치하지 않으면 탈취로 간주
        if (tokenOpt.isEmpty() || !tokenOpt.get().getToken().equals(oldRefreshToken)) {
            log.error("CRITICAL: Refresh Token 탈취 의심이 됩니다!! 사용자:{}, IP:{}", username, getClientIp(request));

            //해당 유저의 모든 Refresh Token삭제하여 모든 세션 무효화
            refreshTokenRepository.deleteByUsername(username);

            //관리자에게 알림 발송하는 로직 호출
            notificationService.notifyAdminOfTokenTheft(username, getClientIp(request));

            return ResponseEntity.status(401).body(new ApiResponseDTO<>(false, "비정상적인 접근이 감지되었습니다.", null));
        }

        //토큰 순환: 새로운 토큰들을 발급하는 로직
        String newAccessToken = jwtUtil.generateAccessToken(username);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        //DB 새로운 Refresh Token저장 (기존 토큰 업데이트)
        RefreshToken savedToken = tokenOpt.get();
        savedToken.setToken(newAccessToken);
        savedToken.setExpiryDate(jwtUtil.getExpirationDateFromToken(newRefreshToken).getTime());
        refreshTokenRepository.save(savedToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(jwtUtil.getRefreshInterval() / 1000)
                .sameSite("Strict") //CRSF 공경 방어
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        AuthResponseDTO authResponseDTO = new AuthResponseDTO(newAccessToken);
        ApiResponseDTO<AuthResponseDTO> responseDTO = new ApiResponseDTO<>(true, "토큰이 성공적으로 갱신되었습니다.", authResponseDTO);

        return ResponseEntity.ok(responseDTO);
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
        public ResponseEntity<ApiResponseDTO<Void>> logout (HttpServletRequest request, HttpServletResponse response){
            String refreshToken = extractRefreshToken(request, "refreshToken");
            if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
                String username = jwtUtil.getUsernameFromToken(refreshToken);
                refreshTokenRepository.deleteByUsername(username);
            }
            ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                    .path("/")
                    .maxAge(0)
                    .build();
            response.addHeader("Set-Cookie", deleteCookie.toString());

            ApiResponseDTO<Void> responseDTO = new ApiResponseDTO<>(true, "로그아웃 되었습니다.", null);

            return ResponseEntity.ok(responseDTO);
        }
    }
