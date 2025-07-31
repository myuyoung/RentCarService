package me.changwook.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.AuthResponseDTO;
import me.changwook.DTO.LoginRequestDTO;
import me.changwook.configuration.config.security.CustomUserDetails;
import me.changwook.configuration.config.security.JwtUtil;
import me.changwook.domain.Member;
import me.changwook.domain.RefreshToken;
import me.changwook.repository.RefreshTokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public Map<String, Object> login(LoginRequestDTO loginRequestDTO) {
        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );

        // CustomUserDetails에서 Member 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = userDetails.getMember();
        
        String username = authentication.getName();
        String role = member.getRole().getAuthority();

        // 권한 정보를 포함한 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(username, role);
        String refreshToken = jwtUtil.generateRefreshToken(username, role);

        // 리프레시 토큰 저장
        refreshTokenRepository.save(RefreshToken.builder()
                .username(username)
                .token(refreshToken)
                .expiryDate(jwtUtil.getExpirationDateFromToken(refreshToken).getTime())
                .build());

        // 사용자 정보를 포함한 응답 생성
        AuthResponseDTO authResponse = AuthResponseDTO.builder()
                .token(accessToken)
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .build();

        Map<String, Object> response = new HashMap<>();
        response.put("authResponse", authResponse);
        response.put("refresh-token", refreshToken);

        log.info("사용자 로그인 성공: {} (권한: {})", member.getEmail(), member.getRole());
        
        return response;
    }
}
