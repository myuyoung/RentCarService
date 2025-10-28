package me.changwook.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.common.ApiResponse;
import me.changwook.common.ResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 【 관리자 전용 유지보수 API 】
 * 
 * 기능:
 * - 데이터 정제 및 수정
 * - 시스템 유지보수 작업
 * - 관리자만 접근 가능
 * 
 * @author changwook
 * @since 1.1
 */
@RestController
@RequestMapping("/api/admin/maintenance")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "관리자 유지보수 API", description = "시스템 유지보수 및 데이터 정제 작업")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMaintenanceController {

    private final ImageMimeTypeFixService imageMimeTypeFixService;
    private final ResponseFactory responseFactory;

    /**
     * 모든 이미지의 MIME 타입을 표준화합니다.
     * 
     * "image/jpg" → "image/jpeg" 등 잘못된 MIME 타입을 수정하여
     * JPG 이미지 표시 문제를 해결합니다.
     */
    @PostMapping("/fix-image-mime-types")
    @Operation(
        summary = "이미지 MIME 타입 표준화", 
        description = "모든 이미지의 MIME 타입을 표준 형식으로 수정합니다. JPG 이미지 표시 문제를 해결할 수 있습니다."
    )
    public ResponseEntity<ApiResponse<String>> fixImageMimeTypes() {
        log.info("=== 관리자 요청: 이미지 MIME 타입 표준화 ===");
        
        try {
            int fixedCount = imageMimeTypeFixService.fixAllImageMimeTypes();
            String message = String.format("이미지 MIME 타입 표준화 완료: %d개 이미지 수정", fixedCount);
            
            return responseFactory.success(message, message);
        } catch (Exception e) {
            log.error("이미지 MIME 타입 표준화 실패", e);
            return responseFactory.internalServerError("MIME 타입 표준화 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 특정 이미지의 MIME 타입을 수정합니다.
     */
    @PostMapping("/fix-image-mime-type/{imageId}")
    @Operation(
        summary = "단일 이미지 MIME 타입 수정", 
        description = "특정 이미지의 MIME 타입을 표준 형식으로 수정합니다."
    )
    public ResponseEntity<ApiResponse<String>> fixImageMimeType(@PathVariable Long imageId) {
        log.info("=== 관리자 요청: 이미지 {} MIME 타입 수정 ===", imageId);
        
        try {
            boolean fixed = imageMimeTypeFixService.fixImageMimeType(imageId);
            
            if (fixed) {
                String message = String.format("이미지 %d의 MIME 타입이 수정되었습니다.", imageId);
                return responseFactory.success(message, message);
            } else {
                String message = String.format("이미지 %d는 수정이 필요하지 않습니다.", imageId);
                return responseFactory.success(message, message);
            }
        } catch (Exception e) {
            log.error("이미지 {} MIME 타입 수정 실패", imageId, e);
            return responseFactory.internalServerError("MIME 타입 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
