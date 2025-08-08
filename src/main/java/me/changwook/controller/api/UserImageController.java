package me.changwook.controller.api;

import lombok.RequiredArgsConstructor;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.service.impl.FileUploadService;
import me.changwook.util.ResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/image")
@RequiredArgsConstructor
public class UserImageController {

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
    public ResponseEntity<ApiResponseDTO<Void>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "memberId", required = false) UUID memberId,
            Authentication authentication) throws IOException {

        // 요청 데이터 전처리
        String uploadedBy = authentication.getName(); // JWT 토큰에서 사용자 정보 추출

        fileUploadService.uploadImage(file, uploadedBy, memberId);

        // 성공 응답 반환 (GlobalExceptionHandler에서 예외 처리)
        return responseFactory.success("이미지 업로드 성공");
    }
}
