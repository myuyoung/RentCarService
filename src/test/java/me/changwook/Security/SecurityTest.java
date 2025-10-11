package me.changwook.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.changwook.DTO.LoginRequestDTO;
import me.changwook.TestRegisterMemberDTO;
import me.changwook.util.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class SecurityTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    private TestRegisterMemberDTO createAccount(){
        return TestRegisterMemberDTO.builder()
                .name("테스트유저")
                .email("testuser@example.com")
                .phone("010-111-2222")
                .password("Test1234!")
                .address("서울시 테스트구")
                .build();
    }

    @Test
    @DisplayName("회원가입 후 로그인 및 JWT를 통한 보호된 API 접근 테스트")
    void registerLoginAndAccessProtectedApi() throws Exception {
        TestRegisterMemberDTO testAccountDTO = createAccount();

        mockMvc.perform(post("/api/register/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAccountDTO)))
                .andExpect(status().isCreated());

        // 2. 로그인 요청 및 JWT 발급
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("testuser@example.com");
        loginRequestDTO.setPassword("Test1234!");

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String jwt = objectMapper.readTree(responseBody).get("data").get("token").asText();
        assertThat(jwt).isNotEmpty();

        // 3. JWT를 이용한 보호된 API 접근 (예시 API)
        mockMvc.perform(get("/api/protected") // 보호된 URL로 변경
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());
    }
}
