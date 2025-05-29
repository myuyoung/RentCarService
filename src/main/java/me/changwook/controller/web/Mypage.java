package me.changwook.controller.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.service.impl.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
@Slf4j
public class Mypage {

    private final MemberService memberService;

    //회원의 개인정보를 확인하는 컨트롤러
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable UUID id , Model model) {
        //서비스 레이어로 변환시켜야 함
        //매퍼로 변환
        model.addAttribute("member",memberService.findById(id));
        log.info("member: {}", memberService.findById(id));
        return "mypage/detail";
    }
}

