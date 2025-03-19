package me.changwook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.MemberDTO;
import me.changwook.domain.Member;
import me.changwook.repository.MemberRepository;
import me.changwook.service.MemberService;
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

    @GetMapping("/detail")
    public String detail(@RequestParam Long id , Model model) {
        Member one = memberRepository.findOne(id).orElseThrow();
        model.addAttribute("member", memberRepository.findOne(id));
        log.info("one");
        return "detail";
    }

    @PostMapping("/member")
    public String member(@Validated @ModelAttribute MemberDTO memberDTO, Model model){
        memberService.create(memberDTO);
        model.addAttribute("member", memberRepository.findOne(memberDTO.getId()));
        log.info("member");
        return "mypage/detail";
    }



}

