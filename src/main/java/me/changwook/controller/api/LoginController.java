package me.changwook.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.DTO.AuthResponseDTO;
import me.changwook.DTO.LoginRequestDTO;
import me.changwook.configuration.config.security.JwtUtil;
import me.changwook.domain.RefreshToken;
import me.changwook.repository.RefreshTokenRepository;
import me.changwook.service.impl.LoginService;
import me.changwook.service.impl.MemberService;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginService loginService;
    private final MemberService memberService;


    /**
     *
     * @param loginRequestDTO 로그인을 할때 입력하는 이메일과 비밀번호를 담아오는 객체
     * @param response HTTP 응답 Header에 쿠키 항목을 추가하기 위한 변수
     * @return ResponseEntity<ApiResponseDTO<AuthResponseDTO>>
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        Map<String, String> token = loginService.login(loginRequestDTO);

        ResponseCookie cookie = ResponseCookie.from("refreshToken",token.get("refresh-token"))
                .httpOnly(true)
                .path("/")
                .maxAge(jwtUtil.getRefreshInterval()/1000)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        AuthResponseDTO accessToken = new AuthResponseDTO(token.get("access_token"));

        ApiResponseDTO<AuthResponseDTO> responseDTO = new ApiResponseDTO<>(true, "로그인이 성공했습니다.", accessToken);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshToken(request,"refreshToken");
        if(!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("토큰이 유효하지 않습니다.");
        }
        String username = jwtUtil.getUsernameFromToken(refreshToken);

        RefreshToken savedToken = refreshTokenRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("데이터 베이스에 Refresh Token 존재하지 않습니다"));

        if(!savedToken.getToken().equals(refreshToken)) {
            throw new RuntimeException("데이터베이스 RefreshToken과 쿠키에 들어있는 RefreshToken이 일치하지 않습니다.");
        }

        String newAccessToken = jwtUtil.generateAccessToken(username);

        AuthResponseDTO authResponseDTO = new AuthResponseDTO(newAccessToken);

        ApiResponseDTO<AuthResponseDTO> responseDTO = new ApiResponseDTO<>(true, "토큰 재생성", authResponseDTO);

        return ResponseEntity.ok(responseDTO);
    }

    private String extractRefreshToken(HttpServletRequest request,String name) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0) {
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshToken(request,"refreshToken");
        if(refreshToken != null && jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            refreshTokenRepository.deleteByUsername(username);
        }
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken","")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", deleteCookie.toString());

        ApiResponseDTO<Void> responseDTO = new ApiResponseDTO<>(true, "로그아웃 되었습니다.", null);

        return ResponseEntity.ok(responseDTO);
    }



}
