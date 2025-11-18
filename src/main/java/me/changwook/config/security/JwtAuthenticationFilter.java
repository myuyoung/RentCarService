package me.changwook.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.member.auth.RefreshToken;
import me.changwook.member.auth.RefreshTokenRepository;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.lang.NonNull;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    private final RefreshTokenRepository refreshTokenRepository;

    private static final String[] EXCLUDE_PATH = {
            "/", "/index.html", "/login", "/logout", "/register", "/auth/login", "/api/register/**",
            "/v3/api-docs/", "/swagger-ui/", "/h2-console/", "/css/", "/js/", "/api/login","/actuator/prometheus"
    };

    private final WebAuthenticationDetailsSource detailsSource = new WebAuthenticationDetailsSource();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String accessToken = getTokenFromHeader(request);

        // 헤더에 토큰이 없으면, 기존 방식대로 쿠키에서 가져옵니다.
        if (!StringUtils.hasText(accessToken)) {
            accessToken = getAccessTokenFromRequest(request); // 기존 쿠키 확인 로직
        }

        if (StringUtils.hasText(accessToken)) {
            try {
                // 토큰 유효성 검증 - ExpiredJwtException 발생 가능
                if (jwtUtil.validateToken(accessToken)) {
                    // 검증 성공 시 인증 설정
                    setAuthentication(accessToken, request);
                }
            } catch (ExpiredJwtException e) {
                log.info("액세스 토큰이 만료되었습니다. 리프레시 토큰으로 재발급 시도합니다.");
                SecurityContextHolder.clearContext();
            }catch (Exception e){
                log.warn("JWT 유효성 검사 실패 {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }

    //URI가 포함되는 순간 filter를 작동시키지 않으므로 인증/인가를 회피할 수 있음.
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String uri = request.getRequestURI();
        boolean shouldSkip = Arrays.stream(EXCLUDE_PATH).anyMatch(path -> {
            // 정확히 일치하거나, 경로가 슬래시로 끝나는 경우는 prefix 매칭 (단, "/" 자체는 정확히 매칭)
            if (path.equals("/")) {
                return uri.equals("/");  // 루트 경로는 정확히 매칭
            } else if (path.endsWith("/")) {
                return uri.startsWith(path);
            }
            return uri.equals(path);
        });

        log.info("JWT Filter shouldNotFilter for URI {}: {}", uri, shouldSkip);

        return shouldSkip;
    }

    //인증 처리 로직 메서드
    private void setAuthentication(String token, HttpServletRequest request) {
        try{
            String username = jwtUtil.getUsernameFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);
            log.debug("토큰에서 추출된 사용자 이름: {}",username);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            log.debug("로드된 UserDetails: {}, 권한:{}", userDetails.getUsername(), userDetails.getAuthorities());

            log.info("JWT 인증 설정: 사용자={}, JWT Role={}, UserDetails Authorities={}", username, role, userDetails.getAuthorities());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(detailsSource.buildDetails(request));


            log.info("Created Authentication object: {}", authentication);
            log.info("Authentication authorities: {}", authentication.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Authentication set in SecurityContextHolder");

            log.info("SecurityContextHolder에 인증 설정 완료: 사용자={}, 권한={}",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        }catch (Exception e){
            log.error("setAuthentication 중 에러 발생: 사용자={}, 토큰={}, 에러={}",
                    jwtUtil.getUsernameFromToken(token), token, e.getMessage(), e);
            SecurityContextHolder.clearContext();
        }

    }

    private String getAccessTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private boolean isRefreshTokenValidInDB(String token){
        String username = jwtUtil.getUsernameFromToken(token);
        return refreshTokenRepository.findByUsername(username).map(refreshToken -> refreshToken.getToken().equals(token)).orElse(false);

    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
