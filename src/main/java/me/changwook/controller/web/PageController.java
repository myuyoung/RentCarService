package me.changwook.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping({"/login","/login/"})
    public String login() {
        return "login";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }

    
    @GetMapping("/search")
    public String search() {
        return "search";
    }

    @GetMapping({"/admin","/admin/"})
    public String admin() {
        return "admin";
    }

    @GetMapping("/admin/car-submissions/{id}")
    public String adminSubmissionDetail() {
        return "admin_submission_detail";
    }

    @GetMapping("/test-image")
    public String testImage() {
        return "test_image";
    }

    @GetMapping("/debug-admin")
    public String debugAdmin() {
        return "debug_admin";
    }
}
