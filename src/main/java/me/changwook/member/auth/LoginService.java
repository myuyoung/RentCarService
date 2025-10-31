package me.changwook.member.auth;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.alert.NotificationService;
import me.changwook.member.dto.AuthResponseDTO;
import me.changwook.member.dto.LoginRequestDTO;
import me.changwook.config.security.CustomUserDetails;
import me.changwook.config.security.JwtUtil;
import me.changwook.member.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import me.changwook.member.Role;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final NotificationService notificationService;

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
        saveOrUpdateRefreshToken(username, refreshToken);

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
    @Transactional
    public Map<String, String> rotateRefreshToken(String oldRefreshToken, String clientIp) throws  ExpiredJwtException, AuthenticationException {

        String username = "";
        String role = "";

        try{
            jwtUtil.validateToken(oldRefreshToken);
            username = jwtUtil.getUsernameFromToken(oldRefreshToken);
            role = jwtUtil.getRoleFromToken(oldRefreshToken);
        }catch(ExpiredJwtException e){
            log.warn("Refresh Token이 만료되었습니다. 재로그인이 필요합니다. 사용자:{}", e.getClaims().getSubject());
            //토큰의 내용이 비어있지 않다면 그러니까 토큰이 존재하는데 만료된 것들을 지움
            if (e.getClaims() != null && e.getClaims().getSubject() != null) {
                refreshTokenRepository.deleteByUsername(e.getClaims().getSubject());
            }
            throw e;
        }catch (Exception e){
            log.warn("유효하지 않은 Refresh Token 입니다. (서명,형식 오류 등", e);
            throw new AuthenticationException("유효하지 않은 RefreshToken 입니다.", e) {
            };
        }

        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByUsername(username);

        // 탈취 감지 로직
        if (tokenOpt.isEmpty() || !tokenOpt.get().getToken().equals(oldRefreshToken)) {
            log.error("CRITICAL: Refresh Token 탈취 의심이 됩니다!! 사용자:{}, IP:{}", username, clientIp);
            refreshTokenRepository.deleteByUsername(username);
            notificationService.notifyAdminOfTokenTheft(username, clientIp);
            throw new AuthenticationException("비정상적인 접근이 감지되었습니다. 다시 로그인해주세요.") {};
        }

        // 토큰 순환: 새로운 토큰들을 발급
        String newAccessToken = jwtUtil.generateAccessToken(username, role);
        String newRefreshToken = jwtUtil.generateRefreshToken(username, role);

        // DB 새로운 Refresh Token저장 (기존 토큰 업데이트)
        RefreshToken savedToken = tokenOpt.get();
        savedToken.setToken(newRefreshToken);
        savedToken.setExpiryDate(jwtUtil.getExpirationDateFromToken(newRefreshToken).getTime());
        refreshTokenRepository.save(savedToken);

        Map<String, String> result = new HashMap<>();
        result.put("accessToken", newAccessToken);
        result.put("refreshToken", newRefreshToken);
        result.put("username", username);
        result.put("role", role);

        return result;
    }

    @Transactional
    public void logout(String refreshToken){
        if(!StringUtils.hasText(refreshToken)){
            return;
        }

        try{
            if(jwtUtil.validateToken(refreshToken)){
                String username = jwtUtil.getUsernameFromToken(refreshToken);
                refreshTokenRepository.deleteByUsername(username);
                log.info("사용자의 {}의 Refresh Token이 DB에서 삭제되었으므로 로그아웃되었습니다.", username);
            }
        }catch(Exception e){
            log.warn("로그아웃 시도 중 토큰 검증 실패(이미 만료되었거나 유효하지 않음): {}",e.getMessage());
        }
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

    @Transactional
    public void saveOrUpdateRefreshToken(String username, String refreshToken) {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUsername(username);
        if (existingToken.isPresent()) {
            // 기존 토큰이 있으면 새 토큰으로 업데이트
            RefreshToken token = existingToken.get();
            token.setToken(refreshToken);
            token.setExpiryDate(jwtUtil.getExpirationDateFromToken(refreshToken).getTime());
            refreshTokenRepository.save(token);
        } else {
            // 기존 토큰이 없으면 새로 생성
            refreshTokenRepository.save(RefreshToken.builder()
                    .username(username)
                    .token(refreshToken)
                    .expiryDate(jwtUtil.getExpirationDateFromToken(refreshToken).getTime())
                    .build());
        }
    }
}
