package me.changwook.configuration.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/templates/**", "/css/**", "/js/**", "/favicon.ico", "/favicon.svg", "/login", "/logout", "/register/**", "/auth/login", "/api/register/member",
                                "/api/rentcars/**",
                                // 공개 페이지: 마이페이지 뷰(내부 API는 계속 보호)
                                "/mypage",
                                // 검색 결과 페이지
                                "/search",
                                // 채팅 페이지
                                "/chat",
                                // 테스트 페이지
                                "/test-image", "/debug-admin",
                                // 이미지 서빙 테스트 API
                                "/api/test/**",
                                // 정적 이미지 리소스 - 공개 접근 허용 (빠른 로딩)
                                "/images/**",
                                // Swagger UI 허용 경로
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/h2-console/**",
                                "/actuator/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/register/").permitAll()
                        // API 기반 파일 스트리밍 - 인증된 사용자만 접근 가능
                        .requestMatchers("/api/files/view/**")
                        .authenticated()
                        // 관리자 전용 API & 페이지
                        .requestMatchers("/api/admin/**", "/admin", "/admin/**")
                        .hasAuthority("ROLE_ADMIN")
                        .anyRequest()
                        .authenticated()
                )
                .headers(headers-> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
