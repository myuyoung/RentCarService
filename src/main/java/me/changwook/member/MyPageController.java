package me.changwook.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.common.ApiResponse;
import me.changwook.member.dto.MemberDTO;
import me.changwook.reservation.dto.ReservationDTO;
import me.changwook.admin.dto.CarRegistrationSubmissionDTO;
import me.changwook.config.security.CustomUserDetails;
import me.changwook.admin.CarRegistrationSubmission;
import me.changwook.reservation.ReservationService;
import me.changwook.admin.CarRegistrationSubmissionService;
import me.changwook.image.FileUploadService;
import me.changwook.common.ResponseFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/MyPage")
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final MemberService memberService;
    private final ReservationService reservationService;
    private final ResponseFactory responseFactory;
    private final CarRegistrationSubmissionService submissionService;
    private final FileUploadService fileUploadService;

    //회원 정보를 조회하는 컨트롤러
    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberDTO>> memberInformation(@PathVariable UUID memberId){

        MemberDTO memberDTO = memberService.findById(memberId);

        return responseFactory.success("회원정보 조회 성공했습니다.", memberDTO);
    }

    //회원 정보를 수정하는 컨트롤러
    @PostMapping("/{memberId}/change")
    public ResponseEntity<ApiResponse<Void>> changeMemberInformation(@PathVariable UUID memberId, @RequestBody MemberDTO memberDTO){
        memberService.update(memberId, memberDTO);

        return responseFactory.success("변경 완료되었습니다.");
    }

    /**
     * 차량을 예약하는 로직
     * @param reservationDTO RentDTO와 RentCarsDTO를 담은 DTO
     * @param userDetails 인증된 사용자 정보를 추출하기 위한 변수
     * @return ResponseEntity<ApiResponse<ReservationDTO>>
     */
    @PostMapping("/reservation")
    public ResponseEntity<ApiResponse<ReservationDTO>> reserveCar(
            @Validated @RequestBody ReservationDTO reservationDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UUID id = userDetails.getMember().getId();

        ReservationDTO createDTO = reservationService.reservation(reservationDTO,id);

        return responseFactory.created("차량 예약 성공하였습니다", createDTO);
    }

    /**
     * 차량 등록 신청 API (이미지 파일과 함께)
     * @param carName 차량명
     * @param rentCarNumber 차량번호
     * @param rentPrice 일일요금
     * @param images 차량 이미지 파일들
     * @param userDetails 인증된 사용자 정보
     * @return 신청 완료 응답
     */
    @PostMapping(value = "/car-submission", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<Void>> submitCarRegistration(
            @RequestParam("carName") String carName,
            @RequestParam("rentCarNumber") String rentCarNumber,
            @RequestParam("rentPrice") Integer rentPrice,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        try {
            UUID memberId = userDetails.getMember().getId();
            
            // 1. 차량 등록 신청 생성
            CarRegistrationSubmissionDTO submissionDTO = CarRegistrationSubmissionDTO.builder()
                    .memberId(memberId)
                    .carName(carName)
                    .rentCarNumber(rentCarNumber)
                    .rentPrice(rentPrice)
                    .build();
            
            CarRegistrationSubmission submission = submissionService.createSubmission(submissionDTO);
            
            // 2. 이미지 파일들이 있으면 업로드하고 신청에 연결
            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        fileUploadService.uploadImage(image, userDetails.getUsername(), null, submission.getId());
                    }
                }
            }
            
            return responseFactory.created("차량 등록 신청이 완료되었습니다.");
            
        } catch (Exception e) {
            log.error("차량 등록 신청 중 오류 발생", e);
            return responseFactory.internalServerError("차량 등록 신청 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    //현재 시간이후의 예약들을 조회하는 컨트롤러
    @GetMapping("/reservation/list")
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> getReservationList(
            @AuthenticationPrincipal CustomUserDetails userDetails){

        UUID memberId = userDetails.getMember().getId();

        List<ReservationDTO> reservation = reservationService.findReservationList(memberId);

        return responseFactory.success("조회가 성공했습니다.", reservation);
    }

    //현재 시간이후의 선택한 예약을 조회하는 컨트롤러
    @GetMapping("/reservation/list/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationDTO>> getReservation(
            @PathVariable UUID reservationId){

        ReservationDTO reservation = reservationService.findReservation(reservationId);

        return responseFactory.success("조회를 성공했습니다.", reservation);
    }

    /**
     * 예약정보를 조회한 페이지에서 요청한 취소요청을 처리하는 컨트롤러
     * @param reservationId 취소할 예약정보를 식별할 기본키(primary key)
     * @param userDetails 인증된 사용자의 정보를 받아오는 변수
     * @return ResponseEntity<ApiResponse<Void>>
     */
    @DeleteMapping("/reservation/list/cancel/{reservationId}")
    public ResponseEntity<ApiResponse<Void>> cancelReservation(
            @PathVariable UUID reservationId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        UUID memberId = userDetails.getMember().getId();

        reservationService.cancelReservation(memberId,reservationId);

        return responseFactory.success("예약이 성공적으로 취소되었습니다.");
    }

    // 차량 등록 신청 생성 (사용자)
    @PostMapping("/car-submissions")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createCarSubmission(
            @RequestBody CarRegistrationSubmissionDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID memberId = userDetails.getMember().getId();
        dto.setMemberId(memberId);
        var saved = submissionService.createSubmission(dto);
        java.util.Map<String, Object> data = java.util.Map.of("id", saved.getId());
        return responseFactory.created("차량 등록 신청이 접수되었습니다.", data);
    }

}
