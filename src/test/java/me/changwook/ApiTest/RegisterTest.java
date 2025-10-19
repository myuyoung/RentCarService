package me.changwook.ApiTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.changwook.DTO.LoginRequestDTO;
import me.changwook.DTO.RegisterMemberDTO;
import me.changwook.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class RegisterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자는 회원가입 후 로그인하여 마이페이지에 접근할 수 있다.")
    void userFullScenarioTest() throws Exception {
        //given(회원가입 시도, 성공 확인)
        RegisterMemberDTO registerMemberDTO = RegisterMemberDTO.builder()
                        .name("testName")
                        .password("Password123!")
                        .email("test123@mail.com")
                        .phone("01011112222")
                        .address("TestAddress")
                        .build();

        String requestBody = objectMapper.writeValueAsString(registerMemberDTO);

        //when
        mockMvc.perform(post("/api/register/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                // then (검증)
                .andExpect(status().isCreated()) // 201 Created 응답을 기대
                .andDo(print());

        //then(데이터베이스에 제대로 저장되었는지 점검)
        assertThat(memberRepository.existsByEmail("test123@mail.com")).isTrue();

        //given(로그인 시도, 토큰 발급 확인)
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test123@mail.com");
        loginRequestDTO.setPassword("Password123!");

        String loginRequestBody = objectMapper.writeValueAsString(loginRequestDTO);

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestBody))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        //응답에서 accessToken추출
        String responseBody = loginResult.getResponse().getContentAsString();
        //간단하게 문자열 파싱으로 토큰을 추출
        String accessToken = objectMapper.readTree(responseBody).get("data").get("token").asText();

        assertThat(accessToken).isNotNull();

        mockMvc.perform(get("/api/protected")
                .header("Authorization", "Bearer " + accessToken))
                //then (검증)
                .andExpect(status().isOk())
                .andDo(print());
    }
}
