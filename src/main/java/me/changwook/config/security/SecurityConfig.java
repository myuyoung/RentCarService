package me.changwook.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // 관리자 전용 API & 페이지
                        .requestMatchers("/api/admin/**", "/admin", "/admin/**")
                        .hasAuthority("ROLE_ADMIN")

                        // USER 권한이 필요한 API
                        .requestMatchers("/api/protected", "/api/protected/**")
//                        .hasAuthority("ROLE_USER")
                                .authenticated()

                        // 그 외 인증이 필요한 API
                        .requestMatchers("/api/files/view/**")
                        .authenticated()

                        // permitAll() 규칙은 가장 마지막에 두는 것이 안전합니다.
                        .requestMatchers("/", "/templates/**", "/css/**", "/js/**", "/favicon.ico", "/favicon.svg", "/login", "/logout", "/register/**", "/auth/login",
                                "/auth/refresh-token",
                                "/api/register/member",
                                "/api/rentcars/**",
                                "/mypage",
                                "/search",
                                "/chat",
                                "/test-image", "/debug-admin",
                                "/api/test/**",
                                "/images/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/h2-console/**",
                                "/actuator/**")
                        .permitAll()

                        // 위에 명시되지 않은 나머지 모든 요청은 인증을 요구합니다.
                        .anyRequest()
                        .authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint))
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
