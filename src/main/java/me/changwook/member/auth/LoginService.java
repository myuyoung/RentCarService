package me.changwook.member.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.member.dto.AuthResponseDTO;
import me.changwook.member.dto.LoginRequestDTO;
import me.changwook.config.security.CustomUserDetails;
import me.changwook.config.security.JwtUtil;
import me.changwook.member.Member;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import me.changwook.member.Role;

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
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUsername(username);
        if (existingToken.isPresent()) {
            // 기존 토큰이 있으면 새 토큰으로 업데이트
            RefreshToken token = existingToken.get();
            token.setToken(refreshToken);
            token.setExpiryDate(jwtUtil.getExpirationDateFromToken(refreshToken).getTime());
            refreshTokenRepository.save(token);
        } else {
            refreshTokenRepository.save(RefreshToken.builder()
                    .username(username)
                    .token(refreshToken)
                    .expiryDate(jwtUtil.getExpirationDateFromToken(refreshToken).getTime())
                    .build());
        }

        // 권한에 따른 리다이렉트 URL 결정
        String redirectUrl = determineRedirectUrl(member.getRole());

        // 사용자 정보를 포함한 응답 생성
        AuthResponseDTO authResponse = AuthResponseDTO.builder()
                .token(accessToken)
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .redirectUrl(redirectUrl) 
                .build();

        Map<String, Object> response = new HashMap<>();
        response.put("authResponse", authResponse);
        response.put("refresh-token", refreshToken);

        log.info("사용자 로그인 성공: {} (권한: {}, Authority: {}, 리다이렉트: {})",
                member.getEmail(), member.getRole(), role, redirectUrl);
        
        return response;
    }

    /**
     * 사용자 권한에 따른 리다이렉트 URL 결정
     */
    private String determineRedirectUrl(Role role) {
        return switch (role) {
            case ADMIN -> "/admin";  
            case USER -> "/";    
            default -> "/";                    
        };
    }
}
