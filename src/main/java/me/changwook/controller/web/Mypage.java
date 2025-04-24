package me.changwook.controller.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.MemberDTO;
import me.changwook.domain.Member;
import me.changwook.repository.MemberRepository;
import me.changwook.service.impl.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
@Slf4j
public class Mypage {

    private final MemberRepository memberRepository;

    private final MemberService memberService;

    //회원의 개인정보를 확인하는 컨트롤러
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id , Model model) {
        //서비스 레이어로 변환시켜야 함
        //매퍼로 변환
        model.addAttribute("member",memberService.inquiry(id));
        log.info("member: {}", memberService.inquiry(id));
        return "/detail";
    }
}

