package me.changwook.controller.api;

import lombok.RequiredArgsConstructor;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.DTO.RegisterMemberDTO;
import me.changwook.service.impl.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.CREATED;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegisterController {

    private final RegisterService registerService;

    //Api 회원가입 컨트롤러
    @PostMapping("/member")
    public ResponseEntity<ApiResponseDTO<Void>> register(@Validated @RequestBody RegisterMemberDTO registerMemberDTO) {

        registerService.registerMember(registerMemberDTO);
        return ResponseEntity.status(CREATED).body(new ApiResponseDTO<>(true,"회원 가입이 성공했습니다.",null));
    }

}
