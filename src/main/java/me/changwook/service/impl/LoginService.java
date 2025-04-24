package me.changwook.service.impl;

import lombok.RequiredArgsConstructor;
import me.changwook.DTO.LoginRequestDTO;
import me.changwook.configuration.config.security.JwtUtil;
import me.changwook.domain.RefreshToken;
import me.changwook.repository.RefreshTokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public Map<String,String> login(LoginRequestDTO loginRequestDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );
        String accessToken = jwtUtil.generateAccessToken(authentication.getName());
        String refreshToken = jwtUtil.generateRefreshToken(authentication.getName());

        //리프레쉬토큰 저장
        refreshTokenRepository.save(RefreshToken.builder()
                .username(authentication.getName())
                .token(refreshToken)
                .expiryDate(jwtUtil.getExpirationDateFromToken(refreshToken).getTime())
                .build());

        Map<String,String> token = new HashMap<>();
        token.put("access_token", accessToken);
        token.put("refresh-token",refreshToken);

        return token;
    }
}
