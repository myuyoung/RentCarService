package me.changwook.image;

import me.changwook.common.ApiResponse;
import me.changwook.common.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 【 계층 역할 】
 * - HTTP 요청/응답 처리 및 클라이언트와의 인터페이스 담당
 * - 요청 파라미터 검증 및 변환
 * - 응답 데이터 포맷팅 및 HTTP 상태 코드 설정
 * - 비즈니스 로직 호출 및 예외 처리
 * 
 * 【 보안 정책 】
 * - SecurityConfig에서 /api/admin/** 경로에 ADMIN 권한 필요하도록 설정
 * - JWT 토큰을 통한 관리자 인증 확인
 * 
 * 【 RESTful API 설계 】
 * - POST   /api/admin/images/upload      : 이미지 업로드
 * - DELETE /api/admin/images/{imageId}   : 이미지 삭제  
 * - GET    /api/admin/images/my-images   : 이미지 목록 조회
 */
@RestController
@RequestMapping("/api/admin/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final FileUploadService fileUploadService;
    private final ResponseFactory responseFactory;
    
    /**
     * 【 프레젠테이션 계층 】이미지 업로드 API
     * 
     * 담당 업무:
     * 1. HTTP 요청 파라미터 수집 및 검증
     * 2. 인증 정보 추출 (JWT 토큰에서 사용자 정보)
     * 3. 비즈니스 계층 호출 (FileUploadService.uploadImage())
     * 4. 응답 데이터 포맷팅 및 HTTP 상태 코드 설정
     * 5. 예외 상황 처리 및 에러 응답 생성
     * 
     * @param file 업로드할 이미지 파일 (multipart/form-data)
     * @param authentication Spring Security 인증 객체
     * @return 업로드 결과 및 이미지 메타데이터
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Void>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "memberId", required = false) UUID memberId,
            @RequestParam(value = "submissionId", required = false) UUID submissionId,
            Authentication authentication) throws IOException {

        // 요청 데이터 전처리
        String uploadedBy = authentication.getName(); // JWT 토큰에서 사용자 정보 추출

        fileUploadService.uploadImage(file, uploadedBy, memberId, submissionId);

        // 성공 응답 반환 (GlobalExceptionHandler에서 예외 처리)
        return responseFactory.success("이미지 업로드 성공");
    }

    /**
     * 【 프레젠테이션 계층 】이미지 삭제 API
     * 
     * 담당 업무:
     * 1. URL 경로에서 이미지 ID 추출
     * 2. 관리자 권한 확인 (Spring Security 자동 처리)
     * 3. 비즈니스 계층 호출 (FileUploadService.deleteImage())
     * 4. 삭제 결과에 따른 HTTP 응답 생성
     * 
     * @param imageId 삭제할 이미지의 고유 ID
     * @param authentication Spring Security 인증 객체
     * @return 삭제 결과 메시지
     */
    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse<Void>> deleteImage(
        @PathVariable Long imageId,
        Authentication authentication) throws IOException {

        // 비즈니스 계층 호출
        boolean deleted = fileUploadService.deleteImage(imageId);

        // 결과에 따른 응답 생성 (GlobalExceptionHandler에서 예외 처리)
        if (deleted) {
            return responseFactory.success("이미지 삭제 성공");
        } else {
            return responseFactory.internalServerError("이미지 삭제가 실패했습니다.");
        }
    }

    /**
     * 【 프레젠테이션 계층 】관리자용 이미지 목록 조회 API
     * 
     * ✅ 담당 업무:
     * 1. 쿼리 파라미터 수집 및 기본값 설정
     * 2. 관리자 권한 확인 (Spring Security 자동 처리)
     * 3. 비즈니스 계층 호출 (추후 구현 예정)
     * 4. 조회 결과 페이징 및 포맷팅
     * 
     * TODO: 향후 구현 예정 기능
     * - 이미지 유형별 필터링
     * - 업로드 일자별 조회
     * - 페이징 처리
     * - 검색 기능
     * 
     * @param authentication Spring Security 인증 객체
     * @return 이미지 목록 및 메타데이터
     */
    @GetMapping("/member-images")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyImages(Authentication authentication) {
        // 📝 추후 구현 예정: 비즈니스 계층에서 이미지 목록 조회 로직 호출
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("message", "관리자용 이미지 목록 조회 기능");
        responseData.put("status", "개발 예정");
        
        return responseFactory.success("추후 구현 예정", responseData);
    }
}