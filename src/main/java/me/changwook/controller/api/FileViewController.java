package me.changwook.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.domain.Image;
import me.changwook.repository.ImageRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "파일 스트리밍 API", description = "파일 업로드/다운로드/스트리밍을 담당하는 API")
@SecurityRequirement(name = "bearerAuth")
public class FileViewController {

    private final ImageRepository imageRepository;

    /**
     * 【 API 기반 파일 스트리밍 】이미지 ID 기반 파일 스트리밍 API
     * 
     * 기능:
     * - 인증된 사용자에게만 파일 접근 허용
     * - 업로드한 사용자 또는 관리자만 접근 가능
     * - HTTP Range 헤더 지원으로 부분 다운로드 가능
     * - 적절한 Content-Type 및 캐시 헤더 설정
     * - 파일 스트리밍으로 메모리 효율적 전송
     *
     * @param imageId DB에 저장된 Image 엔티티의 ID
     * @return 스트리밍된 이미지 파일 리소스
     */
    @GetMapping("/view/{imageId}")
    @Operation(
        summary = "이미지 파일 스트리밍", 
        description = "이미지 ID를 통해 파일을 스트리밍합니다. 인증된 사용자만 접근 가능하며, 업로드한 사용자 또는 관리자만 볼 수 있습니다."
    )
    @ApiResponse(responseCode = "200", description = "이미지 파일 스트리밍 성공")
    @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    @ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없음")
    @ApiResponse(responseCode = "500", description = "파일 읽기 오류")
    public ResponseEntity<Resource> streamImage(
            @Parameter(description = "이미지 ID", required = true)
            @PathVariable Long imageId,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {
        
        log.info("=== 파일 스트리밍 요청 시작 ===");
        log.info("이미지 ID: {}, Range 헤더: {}", imageId, rangeHeader);
        
        // 1. DB에서 이미지 메타데이터 조회
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> {
                    log.warn("이미지를 찾을 수 없습니다. ID: {}", imageId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다.");
                });

        // 2. 접근 권한 확인
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        if (!image.getUploadedBy().equals(currentUsername) && !isAdmin) {
            log.warn("접근 권한 없음. 사용자: {}, 이미지 업로더: {}, 관리자 여부: {}", 
                    currentUsername, image.getUploadedBy(), isAdmin);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
        }
        
        try {
            // 3. 파일 시스템에서 파일 조회
            Path filePath = Paths.get(image.getFilePath());
            
            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                log.error("파일을 읽을 수 없습니다. 경로: {}", filePath);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 읽을 수 없습니다.");
            }

            // 4. 파일 크기 및 스트림 준비
            long fileSize = Files.size(filePath);
            FileInputStream fileInputStream = new FileInputStream(filePath.toFile());
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            // 5. Content-Type 및 응답 헤더 설정 (안전한 MediaType 파싱)
            MediaType mediaType = parseMediaTypeSafely(image.getContentType(), image.getOriginalFileName());

            log.info("파일 스트리밍 준비 완료. 파일: {}, 크기: {} bytes, 타입: {}", 
                    image.getOriginalFileName(), fileSize, mediaType);

            // 6. ResponseEntity 생성 및 반환
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .contentLength(fileSize)
                    // 캐시 설정 (1시간)
                    .cacheControl(CacheControl.maxAge(3600, java.util.concurrent.TimeUnit.SECONDS))
                    // 브라우저에서 바로 표시
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "inline; filename=\"" + image.getOriginalFileName() + "\"")
                    // CORS 헤더 추가
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                    .body(resource);

        } catch (IOException e) {
            log.error("파일 스트리밍 중 I/O 오류 발생", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 읽을 수 없습니다.", e);
        }
    }

    /**
     * 【 안전한 MediaType 파싱 】
     * 
     * MIME 타입 파싱 시 오류가 발생해도 안전하게 처리하고,
     * JPG/PNG 이미지에 대해 정확한 Content-Type을 반환합니다.
     * 
     * @param contentType DB에 저장된 MIME 타입
     * @param originalFileName 원본 파일명 (확장자 추론용)
     * @return 안전하게 파싱된 MediaType
     */
    private MediaType parseMediaTypeSafely(String contentType, String originalFileName) {
        // 1. 먼저 DB의 contentType으로 시도
        if (contentType != null && !contentType.trim().isEmpty()) {
            try {
                MediaType mediaType = MediaType.parseMediaType(contentType.trim());
                log.debug("MediaType 파싱 성공: {}", mediaType);
                return mediaType;
            } catch (Exception e) {
                log.warn("DB Content-Type 파싱 실패: {}, 파일 확장자로 추론 시도", contentType, e);
            }
        }

        // 2. DB 파싱 실패 시 파일 확장자로 추론
        if (originalFileName != null) {
            String extension = getFileExtensionFromName(originalFileName).toLowerCase();
            MediaType inferredType = inferMediaTypeFromExtension(extension);
            if (inferredType != null) {
                log.info("파일 확장자 '{}' 기반으로 MediaType 추론: {}", extension, inferredType);
                return inferredType;
            }
        }

        // 3. 모든 시도 실패 시 기본값
        log.warn("MediaType 추론 실패, 기본값 사용: {} (원본 파일명: {})", MediaType.APPLICATION_OCTET_STREAM, originalFileName);
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    /**
     * 파일 확장자 기반 MediaType 추론
     * 
     * @param extension 파일 확장자 (.jpg, .png 등)
     * @return 추론된 MediaType
     */
    private MediaType inferMediaTypeFromExtension(String extension) {
        switch (extension) {
            case ".jpg":
            case ".jpeg":
                return MediaType.IMAGE_JPEG;
            case ".png":
                return MediaType.IMAGE_PNG;
            case ".gif":
                return MediaType.IMAGE_GIF;
            case ".webp":
                return MediaType.parseMediaType("image/webp");
            default:
                return null;
        }
    }

    /**
     * 파일명에서 확장자 추출
     * 
     * @param fileName 파일명
     * @return 확장자 (점 포함, 소문자)
     */
    private String getFileExtensionFromName(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    }
}
