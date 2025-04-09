package me.changwook.controller.web;

import lombok.RequiredArgsConstructor;
import me.changwook.DTO.RegisterMemberDTO;
import me.changwook.service.impl.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final MemberService memberService;

    @PostMapping("/detail")
    public String registerMember(@Validated @ModelAttribute RegisterMemberDTO registerMemberDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/register/detail";
        }
        memberService.registerMember(registerMemberDTO);
        return "redirect:/login";
    }
}
