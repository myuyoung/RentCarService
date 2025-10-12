package me.changwook.controller.api;

import me.changwook.DTO.MemberDTO;
import me.changwook.configuration.config.security.JwtUtil;
import me.changwook.service.impl.CarRegistrationSubmissionService;
import me.changwook.service.impl.MemberService;
import me.changwook.service.impl.RentCarService;
import me.changwook.service.impl.RentService;
import me.changwook.util.ResponseFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@ActiveProfiles("test")
@Import(SecurityException.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private RentService rentService;

    @MockBean
    private RentCarService rentCarService;

    @MockBean
    private CarRegistrationSubmissionService submissionService;

    @MockBean
    private ResponseFactory responseFactory;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("관리자 페이지 접근 테스트")
    @WithMockUser(roles = "ADMIN")
    void admin_page_access() throws Exception {
        Page<MemberDTO> mockPage = new PageImpl<>(List.of());
        
        when(memberService.getAllMembers(any(Pageable.class))).thenReturn(mockPage);
        when(responseFactory.success(anyString(), any())).thenReturn(
                org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/api/admin/members"))
                .andExpect(status().isOk());
    }
}