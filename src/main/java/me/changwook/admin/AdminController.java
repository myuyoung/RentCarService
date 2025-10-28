package me.changwook.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.common.ApiResponse;
import me.changwook.admin.dto.CarRegistrationSubmissionViewDTO;
import me.changwook.member.dto.MemberDTO;
import me.changwook.reservation.dto.ReservationDTO;
import me.changwook.member.Role;
import me.changwook.member.MemberService;
import me.changwook.rentcar.RentCarService;
import me.changwook.reservation.ReservationService;
import me.changwook.common.ResponseFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "관리자 API", description = "관리자 전용 기능을 제공하는 API")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final MemberService memberService;
    private final ReservationService reservationService;
    private final RentCarService rentCarService;
    private final ResponseFactory responseFactory;
    private final CarRegistrationSubmissionService submissionService;

    @GetMapping("/members")
    @Operation(summary = "전체 회원 조회", description = "모든 회원 정보를 페이징하여 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<MemberDTO>>> getAllMembers(Pageable pageable) {
        log.info("관리자가 전체 회원 목록을 조회합니다. 페이지: {}, 크기: {}", 
                 pageable.getPageNumber(), pageable.getPageSize());
        
        Page<MemberDTO> members = memberService.getAllMembers(pageable);
        
        return responseFactory.success("전체 회원 조회 성공", members);
    }

    @GetMapping("/members/{memberId}")
    @Operation(summary = "특정 회원 조회", description = "회원 ID로 특정 회원 정보를 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MemberDTO>> getMemberById(@PathVariable UUID memberId) {
        log.info("관리자가 회원 정보를 조회합니다. 회원 ID: {}", memberId);
        
        MemberDTO member = memberService.getMemberById(memberId);
        
        return responseFactory.success("회원 조회 성공", member);
    }

    @PutMapping("/members/{memberId}/role")
    @Operation(summary = "회원 권한 변경", description = "회원의 권한을 변경합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MemberDTO>> updateMemberRole(
            @PathVariable UUID memberId, 
            @RequestBody Map<String, String> roleUpdate) {
        
        log.info("관리자가 회원 권한을 변경합니다. 회원 ID: {}, 새 권한: {}", 
                 memberId, roleUpdate.get("role"));
        
        Role newRole = Role.valueOf(roleUpdate.get("role"));
        MemberDTO updatedMember = memberService.updateMemberRole(memberId, newRole);
        
        return responseFactory.success("회원 권한 변경 성공", updatedMember);
    }

    @DeleteMapping("/members/{memberId}")
    @Operation(summary = "회원 삭제", description = "회원을 삭제합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable UUID memberId) {
        log.info("관리자가 회원을 삭제합니다. 회원 ID: {}", memberId);
        
        memberService.deleteMember(memberId);
        
        return responseFactory.success("회원 삭제 성공");
    }

    @GetMapping("/rentals")
    @Operation(summary = "전체 예약 조회", description = "모든 예약 정보를 페이징하여 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<ReservationDTO>>> getAllRentals(Pageable pageable) {
        log.info("관리자가 전체 예약 목록을 조회합니다. 페이지: {}, 크기: {}", 
                 pageable.getPageNumber(), pageable.getPageSize());
        
        Page<ReservationDTO> rentals = reservationService.getAllRentals(pageable);
        
        return responseFactory.success("전체 예약 조회 성공", rentals);
    }



    // --- 차량 등록 신청 관리 ---
    @GetMapping("/car-submissions")
    @Operation(summary = "차량 등록 신청 목록", description = "PENDING 상태의 신청 목록을 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<CarRegistrationSubmissionViewDTO>>> listSubmissions(Pageable pageable) {
        Page<CarRegistrationSubmissionViewDTO> page = submissionService.listPending(pageable);
        return responseFactory.success("차량 등록 신청 목록 조회 성공", page);
    }

    @GetMapping("/car-submissions/{submissionId}")
    @Operation(summary = "차량 등록 신청 상세", description = "특정 신청의 상세 정보를 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CarRegistrationSubmissionViewDTO>> getSubmission(
            @PathVariable java.util.UUID submissionId) {
        var dto = submissionService.getSubmissionDetail(submissionId);
        return responseFactory.success("차량 등록 신청 상세 조회 성공", dto);
    }

    @PostMapping("/car-submissions/{submissionId}/approve")
    @Operation(summary = "차량 등록 신청 승인", description = "신청을 승인하고 차량으로 등록합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> approveSubmission(@PathVariable java.util.UUID submissionId) {
        submissionService.approveSubmission(submissionId);
        return responseFactory.success("신청이 승인되어 차량이 등록되었습니다.");
    }

    @PostMapping("/car-submissions/{submissionId}/reject")
    @Operation(summary = "차량 등록 신청 반려", description = "신청을 반려합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> rejectSubmission(@PathVariable java.util.UUID submissionId) {
        submissionService.rejectSubmission(submissionId);
        return responseFactory.success("신청이 반려되었습니다.");
    }



    @GetMapping("/statistics")
    @Operation(summary = "시스템 통계", description = "전체 회원 수, 예약 수, 차량 수 등의 통계를 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        log.info("관리자가 시스템 통계를 조회합니다.");
        
        Map<String, Object> statistics = Map.of(
            "totalMembers", memberService.getTotalMemberCount(),
            "totalRentals", reservationService.getTotalRentalCount(),
            "totalCars", rentCarService.getTotalCarCount(),
            "activeRentals", reservationService.getActiveRentalCount()
        );
        
        return responseFactory.success("시스템 통계 조회 성공", statistics);
    }
}