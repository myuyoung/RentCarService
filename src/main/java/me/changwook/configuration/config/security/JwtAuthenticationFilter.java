package me.changwook.configuration.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    private static final String[] EXCLUDE_PATH = {
            "/", "/index.html", "/login", "/logout", "/register", "/auth/login", "/api/register/member",
            "/v3/api-docs/", "/swagger-ui/", "/h2-console/", "/css/", "/js/"
    };

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        logger.info("JWT Filter executing for URI: " + request.getRequestURI());
        String token = getJwtFromRequest(request);
        logger.info("Extracted token: " + (token != null ? "Present" : "Absent"));
        
        if (StringUtils.hasText(token)) {
            try {
                // 토큰 유효성 검증을 먼저 수행
                if (jwtUtil.validateToken(token)) {
                    // 검증된 토큰에서 사용자 정보 추출
                    String username = jwtUtil.getUsernameFromToken(token);
                    
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        logger.info("JWT Authentication - Username: " + username + ", Authorities: " + userDetails.getAuthorities());
                        
                        // 인증 토큰 생성 및 설정
                        UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource().buildDetails(request));
                        
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.info("Authentication set in SecurityContext for: " + username);
                    }
                } else {
                    logger.warn("Invalid JWT token received");
                }
            } catch (Exception e) {
                logger.error("JWT authentication error: " + e.getMessage());
                // 인증 실패 시 SecurityContext 초기화
                SecurityContextHolder.clearContext();
            }
        }
        
        filterChain.doFilter(request, response);
    }

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
        logger.info("JWT Filter shouldNotFilter for URI " + uri + ": " + shouldSkip);
        return shouldSkip;
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        // 1) Authorization 헤더 우선
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // 2) 헤더가 없다면 accessToken 쿠키에서 조회 (페이지 네비게이션 시 자동 첨부)
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName()) && StringUtils.hasText(cookie.getValue())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
