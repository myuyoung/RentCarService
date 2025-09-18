package me.changwook.configuration.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import me.changwook.repository.RefreshTokenRepository;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    private final RefreshTokenRepository refreshTokenRepository;

    private static final String[] EXCLUDE_PATH = {
            "/", "/index.html", "/login", "/logout", "/register", "/auth/login", "/api/register/member",
            "/v3/api-docs/", "/swagger-ui/", "/h2-console/", "/css/", "/js/"
    };

    private final WebAuthenticationDetailsSource detailsSource = new WebAuthenticationDetailsSource();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String accessToken = getAccessTokenFromRequest(request);

        if (StringUtils.hasText(accessToken)) {
            try {
                // 토큰 유효성 검증 - ExpiredJwtException 발생 가능
                jwtUtil.validateToken(accessToken);
                // 검증 성공 시 인증 설정
                setAuthentication(accessToken, request);
            } catch (ExpiredJwtException e) {
                logger.info("액세스 토큰이 만료되었습니다. 리프레시 토큰으로 재발급 시도합니다.");
                String refreshToken = getRefreshTokenFromCookie(request);
                if (refreshToken != null && isRefreshTokenValidInDB(refreshToken)) {
                    // DB에서 리프레시 토큰 확인
                    String username = jwtUtil.getUsernameFromToken(refreshToken);

                    String role = jwtUtil.getRoleFromToken(refreshToken);

                    String newAccessToken = jwtUtil.generateAccessToken(username, role);

                    setAuthentication(newAccessToken, request);

                    response.setHeader("X-New-Access-Token", newAccessToken);

                    logger.info("액세스 토큰이 성공적으로 갱신되었습니다. 사용자: " + username);
                } else {
                    logger.warn("DB에서 유효하지 않은 리프레시 토큰입니다.");

                    SecurityContextHolder.clearContext();
                }
            }catch (Exception e){
                logger.error("JWT 인증 오류 " + e.getMessage());
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

    //인증 처리 로직 메서드
    private void setAuthentication(String token, HttpServletRequest request) {
        String username = jwtUtil.getUsernameFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        logger.info("JWT 인증 설정: 사용자={}, JWT Role={}, UserDetails Authorities={}",
                    username, role, userDetails.getAuthorities());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(detailsSource.buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info("인증 컨텍스트 설정 완료: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
    }

    private String getAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
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
}
