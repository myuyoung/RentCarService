package me.changwook.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.changwook.DTO.AuthResponseDTO;
import me.changwook.DTO.LoginRequestDTO;
import me.changwook.configuration.config.security.JwtUtil;
import me.changwook.domain.Role;
import me.changwook.repository.RefreshTokenRepository;
import me.changwook.service.NotificationService;
import me.changwook.service.impl.LoginService;
import me.changwook.service.impl.MemberService;
import me.changwook.util.ResponseFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @MockBean
    private LoginService loginService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private ResponseFactory responseFactory;

    @Test
    @DisplayName("로그인 성공 테스트")
    @WithMockUser
    void login_success() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        AuthResponseDTO authResponse = AuthResponseDTO.builder()
                .token("mock-access-token")
                .email("test@example.com")
                .name("테스트사용자")
                .role(Role.USER)
                .build();

        Map<String, Object> loginResult = Map.of(
                "authResponse", authResponse,
                "refresh-token", "mock-refresh-token"
        );

        when(loginService.login(any(LoginRequestDTO.class))).thenReturn(loginResult);
        when(jwtUtil.getRefreshInterval()).thenReturn(604800000L);
        when(jwtUtil.getExpiration()).thenReturn(3600000L);
        when(responseFactory.success(any(String.class), any())).thenReturn(
                org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(post("/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"));
    }

    @Test
    @DisplayName("잘못된 로그인 요청 테스트")
    @WithMockUser
    void login_invalid_request() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("");
        loginRequest.setPassword("");

        mockMvc.perform(post("/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그아웃 성공 테스트")
    @WithMockUser
    void logout_success() throws Exception {
        when(jwtUtil.validateToken(any(String.class))).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(any(String.class))).thenReturn("test@example.com");
        when(responseFactory.success(any(String.class))).thenReturn(
                org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(post("/auth/logout")
                .with(csrf())
                .cookie(new jakarta.servlet.http.Cookie("refreshToken", "mock-refresh-token")))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"));
    }
}