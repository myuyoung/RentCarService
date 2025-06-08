package me.changwook.controller.api;

import lombok.RequiredArgsConstructor;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.DTO.MemberDTO;
import me.changwook.DTO.RentDTO;
import me.changwook.DTO.ReservationDTO;
import me.changwook.configuration.config.security.CustomUserDetails;
import me.changwook.service.impl.MemberService;
import me.changwook.service.impl.RentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/MyPage")
@RequiredArgsConstructor
public class MyPageController {

    private final MemberService memberService;

    private final RentService rentService;

    //회원 정보를 조회하는 컨트롤러
    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponseDTO<MemberDTO>> memberInformation(@PathVariable UUID memberId){

        MemberDTO memberDTO = memberService.findById(memberId);

        ApiResponseDTO<MemberDTO> responseDTO = new ApiResponseDTO<>(true,"회원정보 조회 성공했습니다.",memberDTO);

        return ResponseEntity.ok(responseDTO);
    }

    //회원 정보를 수정하는 컨트롤러
    @PostMapping("/{memberId}/change")
    public ResponseEntity<ApiResponseDTO<Void>> changeMemberInformation(@PathVariable UUID memberId, @RequestBody MemberDTO memberDTO){
        memberService.update(memberId, memberDTO);

        ApiResponseDTO<Void> responseDTO = new ApiResponseDTO<>(true,"변경 완료되었습니다.",null);

        return ResponseEntity.ok(responseDTO);
    }

    /**
     * 차량을 예약하는 로직
     * @param reservationDTO RentDTO와 RentCarsDTO를 담은 DTO
     * @param userDetails 인증된 사용자 정보를 추출하기 위한 변수
     * @return ResponseEntity<ApiResponseDTO<RentDTO>>
     */
    @PostMapping("/reservation")
    public ResponseEntity<ApiResponseDTO<RentDTO>> reserveCar(
            @Validated @RequestBody ReservationDTO reservationDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UUID id = userDetails.getMember().getId();

        RentDTO createDTO = rentService.rentInformation(reservationDTO,id);

        ApiResponseDTO<RentDTO> responseDTO = new ApiResponseDTO<>(true,"차량 예약 성공하였습니다",createDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    //현재 시간이후의 예약들을 조회하는 컨트롤러
    @GetMapping("/reservation/list")
    public ResponseEntity<ApiResponseDTO<List<RentDTO>>> getReservationList(
            @AuthenticationPrincipal CustomUserDetails userDetails){

        UUID memberId = userDetails.getMember().getId();

        List<RentDTO> reservation = rentService.findReservationList(memberId);

        ApiResponseDTO<List<RentDTO>> responseDTO =new ApiResponseDTO<>(true,"조회가 성공했습니다.",reservation);

        return ResponseEntity.ok(responseDTO);
    }

    //현재 시간이후의 선택한 예약을 조회하는 컨트롤러
    @GetMapping("/reservation/list/{reservationId}")
    public ResponseEntity<ApiResponseDTO<RentDTO>> getReservation(
            @PathVariable UUID reservationId){

        RentDTO reservation = rentService.findReservation(reservationId);

        ApiResponseDTO<RentDTO> responseDTO = new ApiResponseDTO<>(true,"조회를 성공했습니다.",reservation);


        return ResponseEntity.ok(responseDTO);
    }

    /**
     * 예약정보를 조회한 페이지에서 요청한 취소요청을 처리하는 컨트롤러
     * @param reservationId 취소할 예약정보를 식별할 기본키(primary key)
     * @param userDetails 인증된 사용자의 정보를 받아오는 변수
     * @return ResponseEntity<ApiResponseDTO<Void>>
     */
    @DeleteMapping("/reservation/list/cancel/{reservationId}")
    public ResponseEntity<ApiResponseDTO<Void>> cancelReservation(
            @PathVariable UUID reservationId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        UUID memberId = userDetails.getMember().getId();

        rentService.cancelReservation(memberId,reservationId);

        ApiResponseDTO<Void> responseDTO = new ApiResponseDTO<>(true,"예약이 성공적으로 취소되었습니다.",null);

        return ResponseEntity.ok(responseDTO);
    }

}
