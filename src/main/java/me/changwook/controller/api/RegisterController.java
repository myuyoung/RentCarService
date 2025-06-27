package me.changwook.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name="1.회원 가입 API", description = "사용자 회원 가입을 처리하는 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegisterController {

    private final RegisterService registerService;

    //Api 회원가입 컨트롤러
    @Operation(summary = "일반 회원 가입", description = "이메일, 비밀번호, 이름 등으로 회원 가입을 요청합니다.")
    @ApiResponse(responseCode = "201", description = "회원가입 성공")
    @PostMapping("/member")
    public ResponseEntity<ApiResponseDTO<Void>> register(@Validated @RequestBody RegisterMemberDTO registerMemberDTO) {

        registerService.registerMember(registerMemberDTO);
        return ResponseEntity.status(CREATED).body(new ApiResponseDTO<>(true,"회원가입이 성공했습니다.",null));
    }

}
