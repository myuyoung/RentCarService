package me.changwook.controller.api;

import lombok.RequiredArgsConstructor;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.DTO.MemberDTO;
import me.changwook.service.impl.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class Mypage {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponseDTO<MemberDTO>> memberInformation(@PathVariable UUID memberId){

        MemberDTO memberDTO = memberService.findById(memberId);

        ApiResponseDTO<MemberDTO> responseDTO = new ApiResponseDTO<>(true,"회원정보 조회 성공했습니다.",memberDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/change")
    public ResponseEntity<ApiResponseDTO<Void>> changeMemberInformation(@RequestBody MemberDTO memberDTO){
        memberService.update(memberDTO);

        ApiResponseDTO<Void> responseDTO = new ApiResponseDTO<>(true,"변경 완료되었습니다.",null);

        return ResponseEntity.ok(responseDTO);
    }

}
