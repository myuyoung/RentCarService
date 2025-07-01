package me.changwook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.changwook.TestRegisterMemberDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class RegisterControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JavaMailSender javaMailSender;

    private TestRegisterMemberDTO createValidDTO() {
        return TestRegisterMemberDTO.builder()
                .name("TestName")
                .email("testuser@example.com")
                .password("Password1234!")
                .phone("010-1111-2222")
                .address("서울시 테스트구")
                .build();
    }

    @Test
    @DisplayName("회원가입 성공 - 유효한 데이터")
    void register_success_withValidDate() throws Exception{
        //Given
        TestRegisterMemberDTO testDTO = createValidDTO();
        String requestBody = objectMapper.writeValueAsString(testDTO);

        //when
        ResultActions actions = mockMvc.perform(post("/api/register/member")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language","ko")
                .content(requestBody));

        //then
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("회원가입이 성공했습니다."))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("회원가입 중 이름을 쓰지 않을 때")
    void register_fail_name() throws Exception{
        //given
        TestRegisterMemberDTO validDTO = createValidDTO();
        validDTO.setName(" ");
        String requestBody = objectMapper.writeValueAsString(validDTO);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/register/member")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language","ko")
                .content(requestBody));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("회원가입이 실패했습니다."))
                .andExpect(jsonPath("$.data.name").value("이름은 비어 있을 수 없습니다."));
    }
}
