package me.changwook.controller.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = PageController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "me.changwook.config.*")
    }
)
@WithMockUser
class PageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void home() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void register() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
        mockMvc.perform(get("/login/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void mypage() throws Exception {
        mockMvc.perform(get("/mypage"))
                .andExpect(status().isOk())
                .andExpect(view().name("mypage"));
    }

    @Test
    void search() throws Exception {
        mockMvc.perform(get("/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"));
    }

    @Test
    void admin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));
        mockMvc.perform(get("/admin/"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));
    }

    @Test
    void adminSubmissionDetail() throws Exception {
        mockMvc.perform(get("/admin/car-submissions/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin_submission_detail"));
    }

    @Test
    void testImage() throws Exception {
        mockMvc.perform(get("/test-image"))
                .andExpect(status().isOk())
                .andExpect(view().name("test_image"));
    }

    @Test
    void debugAdmin() throws Exception {
        mockMvc.perform(get("/debug-admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("debug_admin"));
    }

    @Test
    void chat() throws Exception {
        mockMvc.perform(get("/chat"))
                .andExpect(status().isOk())
                .andExpect(view().name("chat"));
        mockMvc.perform(get("/chat/"))
                .andExpect(status().isOk())
                .andExpect(view().name("chat"));
    }
}
