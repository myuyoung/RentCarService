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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        Map<String, String> token = loginService.login(loginRequestDTO);

        ResponseCookie cookie = ResponseCookie.from("refreshToken",token.get("refresh-token"))
                .httpOnly(true)
                .path("/")
                .maxAge(jwtUtil.getRefreshInterval()/1000)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new ApiResponseDTO<>(true,"로그인이 성공했습니다.",new AuthResponseDTO(token.get("access_token"))));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshToken(request,"refreshToken");
        if(!jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token 만료");
        }
        String username = jwtUtil.getUsernameFromToken(refreshToken);

        RefreshToken savedToken = refreshTokenRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("데이터 베이스에 Refresh Token 존재하지 않습니다"));

        if(!savedToken.getToken().equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 불일치");
        }

        String newAccessToken = jwtUtil.generateAccessToken(username);
        return ResponseEntity.ok(new ApiResponseDTO<>(true,"토큰 재생성",new AuthResponseDTO(newAccessToken)));
    }

    private String extractRefreshToken(HttpServletRequest request,String name) {
        if(request.getParameter(name) != null) {
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
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
        return ResponseEntity.ok(new ApiResponseDTO<>(true,"로그아웃 되었습니다.",null));
    }



}
