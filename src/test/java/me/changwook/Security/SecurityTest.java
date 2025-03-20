package me.changwook.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.changwook.DTO.LoginRequest;
import me.changwook.DTO.RegisterMemberDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//통합 테스트 목적 (회원가입 + 로그인{Spring Security와 JWT})
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 후 로그인 및 JWT를 통한 보호된 API 접근 테스트")
    void registerLoginAndAccessProtectedApi() throws Exception {
        // 1. 회원가입 요청
        RegisterMemberDTO registerDto = new RegisterMemberDTO();
        registerDto.setName("테스트유저");
        registerDto.setEmail("testuser@example.com");
        registerDto.setPassword("Test1234!");

        mockMvc.perform(post("/register/detail")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", registerDto.getName())
                        .param("email", registerDto.getEmail())
                        .param("password", registerDto.getPassword()))
                .andExpect(status().is3xxRedirection());

        // 2. 로그인 요청 및 JWT 발급
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("testuser@example.com");
        loginRequest.setPassword("Test1234!");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String jwt = objectMapper.readTree(responseBody).get("token").asText();
        assertThat(jwt).isNotEmpty();

        // 3. JWT를 이용한 보호된 API 접근 (예시 API)
        mockMvc.perform(get("/api/protected") // 보호된 URL로 변경
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());
    }
}
