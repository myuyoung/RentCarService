package me.changwook.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.DTO.RentCarsDTO;
import me.changwook.service.impl.RentCarService;
import me.changwook.util.ResponseFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 【 RESTful 차량 관리 API 】
 * 
 * 담당 업무:
 * - 차량 등록, 조회, 수정, 삭제 (CRUD)
 * - RESTful 설계 원칙 준수
 * - 관리자 권한 기반 접근 제어
 * 
 * 【 API 엔드포인트 】
 * - POST   /api/admin/cars          : 차량 등록
 * - GET    /api/admin/cars          : 차량 목록 조회 (페이징)
 * - GET    /api/admin/cars/{id}     : 특정 차량 조회
 * - PUT    /api/admin/cars/{id}     : 차량 정보 수정
 * - DELETE /api/admin/cars/{id}     : 차량 삭제
 */
@RestController
@RequestMapping("/api/admin/cars")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "관리자 차량 관리 API", description = "관리자 전용 렌트카 등록, 조회, 수정, 삭제를 담당하는 RESTful API")
@SecurityRequirement(name = "bearerAuth")
public class AdminRentCarController {

    private final RentCarService rentCarService;
    private final ResponseFactory responseFactory;

    /**
     * 【 차량 등록 】
     * POST /api/admin/cars
     */
    @PostMapping
    @Operation(summary = "차량 등록", description = "새로운 렌트카를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "차량 등록 성공")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<RentCarsDTO>> registerCar(@Validated @RequestBody RentCarsDTO rentCarsDTO) {
        // 임시로 기존 메서드 사용
        rentCarService.save(rentCarsDTO);
        
        return responseFactory.created("차량이 성공적으로 등록되었습니다.", rentCarsDTO);
    }

    /**
     * 【 차량 목록 조회 】
     * GET /api/admin/cars
     */
    @GetMapping
    @Operation(summary = "차량 목록 조회", description = "등록된 모든 차량을 페이징하여 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Page<RentCarsDTO>>> getAllCars(Pageable pageable) {
        // 차량 목록 조회
        
        Page<RentCarsDTO> cars = rentCarService.getAllCars(pageable);
        
        return responseFactory.success("차량 목록 조회 성공", cars);
    }

    /**
     * 【 특정 차량 조회 】
     * GET /api/admin/cars/{id}
     */
    @GetMapping("/{carId}")
    @Operation(summary = "특정 차량 조회", description = "차량 ID로 특정 차량 정보를 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<RentCarsDTO>> getCarById(@PathVariable Long carId) {
        // 차량 조회
        
        RentCarsDTO car = rentCarService.getCarById(carId);
        
        return responseFactory.success("차량 조회 성공", car);
    }

    /**
     * 【 차량 정보 수정 】
     * PUT /api/admin/cars/{id}
     */
    @PutMapping("/{carId}")
    @Operation(summary = "차량 정보 수정", description = "기존 차량의 정보를 수정합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<RentCarsDTO>> updateCar(
            @PathVariable Long carId, 
            @Validated @RequestBody RentCarsDTO rentCarsDTO) {
        // 차량 수정
        
        RentCarsDTO updatedCar = rentCarService.updateCar(carId, rentCarsDTO);
        
        return responseFactory.success("차량 정보가 성공적으로 수정되었습니다.", updatedCar);
    }

    /**
     * 【 차량 삭제 】
     * DELETE /api/admin/cars/{id}
     */
    @DeleteMapping("/{carId}")
    @Operation(summary = "차량 삭제", description = "등록된 차량을 삭제합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<Void>> deleteCar(@PathVariable Long carId) {
        // 차량 삭제
        
        rentCarService.deleteRentCar(carId);
        
        return responseFactory.success("차량이 성공적으로 삭제되었습니다.");
    }
}
