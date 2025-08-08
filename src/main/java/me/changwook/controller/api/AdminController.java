package me.changwook.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.DTO.MemberDTO;
import me.changwook.DTO.RentCarsDTO;
import me.changwook.DTO.RentDTO;
import me.changwook.domain.Role;
import me.changwook.service.impl.MemberService;
import me.changwook.service.impl.RentCarService;
import me.changwook.service.impl.RentService;
import me.changwook.util.ResponseFactory;
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
    private final RentService rentService;
    private final RentCarService rentCarService;
    private final ResponseFactory responseFactory;

    @GetMapping("/members")
    @Operation(summary = "전체 회원 조회", description = "모든 회원 정보를 페이징하여 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Page<MemberDTO>>> getAllMembers(Pageable pageable) {
        log.info("관리자가 전체 회원 목록을 조회합니다. 페이지: {}, 크기: {}", 
                 pageable.getPageNumber(), pageable.getPageSize());
        
        Page<MemberDTO> members = memberService.getAllMembers(pageable);
        
        return responseFactory.success("전체 회원 조회 성공", members);
    }

    @GetMapping("/members/{memberId}")
    @Operation(summary = "특정 회원 조회", description = "회원 ID로 특정 회원 정보를 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<MemberDTO>> getMemberById(@PathVariable UUID memberId) {
        log.info("관리자가 회원 정보를 조회합니다. 회원 ID: {}", memberId);
        
        MemberDTO member = memberService.getMemberById(memberId);
        
        return responseFactory.success("회원 조회 성공", member);
    }

    @PutMapping("/members/{memberId}/role")
    @Operation(summary = "회원 권한 변경", description = "회원의 권한을 변경합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<MemberDTO>> updateMemberRole(
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
    public ResponseEntity<ApiResponseDTO<Void>> deleteMember(@PathVariable UUID memberId) {
        log.info("관리자가 회원을 삭제합니다. 회원 ID: {}", memberId);
        
        memberService.deleteMember(memberId);
        
        return responseFactory.success("회원 삭제 성공");
    }

    @GetMapping("/rentals")
    @Operation(summary = "전체 예약 조회", description = "모든 예약 정보를 페이징하여 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Page<RentDTO>>> getAllRentals(Pageable pageable) {
        log.info("관리자가 전체 예약 목록을 조회합니다. 페이지: {}, 크기: {}", 
                 pageable.getPageNumber(), pageable.getPageSize());
        
        Page<RentDTO> rentals = rentService.getAllRentals(pageable);
        
        return responseFactory.success("전체 예약 조회 성공", rentals);
    }

    @GetMapping("/cars")
    @Operation(summary = "전체 차량 조회", description = "모든 차량 정보를 페이징하여 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Page<RentCarsDTO>>> getAllCars(Pageable pageable) {
        log.info("관리자가 전체 차량 목록을 조회합니다. 페이지: {}, 크기: {}", 
                 pageable.getPageNumber(), pageable.getPageSize());
        
        Page<RentCarsDTO> cars = rentCarService.getAllCars(pageable);
        
        return responseFactory.success("전체 차량 조회 성공", cars);
    }

    @DeleteMapping("/cars/{carId}")
    @Operation(summary = "차량 삭제", description = "차량을 삭제합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Void>> deleteCar(@PathVariable Long carId) {
        log.info("관리자가 차량을 삭제합니다. 차량 ID: {}", carId);
        
        rentCarService.deleteRentCar(carId);
        
        return responseFactory.success("차량 삭제 성공");
    }

    @GetMapping("/statistics")
    @Operation(summary = "시스템 통계", description = "전체 회원 수, 예약 수, 차량 수 등의 통계를 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> getStatistics() {
        log.info("관리자가 시스템 통계를 조회합니다.");
        
        Map<String, Object> statistics = Map.of(
            "totalMembers", memberService.getTotalMemberCount(),
            "totalRentals", rentService.getTotalRentalCount(),
            "totalCars", rentCarService.getTotalCarCount(),
            "activeRentals", rentService.getActiveRentalCount()
        );
        
        return responseFactory.success("시스템 통계 조회 성공", statistics);
    }


}