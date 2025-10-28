package me.changwook.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.changwook.common.ApiResponse;
import me.changwook.member.dto.RegisterMemberDTO;
import me.changwook.common.ResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@Tag(name="1.회원 가입 API", description = "사용자 회원 가입을 처리하는 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegisterController {

    private final RegisterService registerService;
    private final ResponseFactory responseFactory;

    //Api 회원가입 컨트롤러
    @Operation(summary = "일반 회원 가입", description = "이메일, 비밀번호, 이름 등으로 회원 가입을 요청합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공")
    @PostMapping("/member")
    public ResponseEntity<ApiResponse<Void>> register(@Validated @RequestBody RegisterMemberDTO registerMemberDTO) {

        registerService.registerMember(registerMemberDTO);
        return responseFactory.created("회원가입이 성공했습니다.");
    }

}
