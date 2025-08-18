package me.changwook.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.domain.Image;
import me.changwook.repository.ImageRepository;
import me.changwook.service.impl.FileUploadService;
import me.changwook.util.ResponseFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API 기반 파일 스트리밍 테스트를 위한 컨트롤러
 * 개발/디버깅 목적으로 사용
 */
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class ImageTestController {
    
    private final FileUploadService fileUploadService;
    private final ResponseFactory responseFactory;
    private final ImageRepository imageRepository;
    
    @Value("${file.upload.dir}")
    private String uploadDir;
    
    @GetMapping("/streaming-config")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> testStreamingConfig() {
        String resolvedUploadDir = uploadDir.replace("${user.home}", System.getProperty("user.home"));
        
        Map<String, Object> config = new HashMap<>();
        config.put("streamingMode", "API 기반 파일 스트리밍");
        config.put("originalUploadDir", uploadDir);
        config.put("resolvedUploadDir", resolvedUploadDir);
        config.put("userHome", System.getProperty("user.home"));
        
        // 디렉터리 존재 여부 확인
        File uploadDirFile = new File(resolvedUploadDir);
        config.put("uploadDirExists", uploadDirFile.exists());
        config.put("uploadDirIsDirectory", uploadDirFile.isDirectory());
        
        // DB에서 이미지 목록 조회
        List<Image> images = imageRepository.findAll();
        config.put("totalImagesInDB", images.size());
        
        if (!images.isEmpty()) {
            Image firstImage = images.get(0);
            config.put("firstImageId", firstImage.getId());
            config.put("firstImageName", firstImage.getOriginalFileName());
            config.put("firstImagePath", firstImage.getFilePath());
            
            // API 기반 스트리밍 URL 생성 테스트
            String streamingUrl = fileUploadService.getImageStreamUrl(firstImage.getId());
            config.put("generatedStreamingUrl", streamingUrl);
            config.put("fullStreamingUrl", "http://localhost:7950" + streamingUrl);
            
            // 실제 파일 존재 여부 확인
            File actualFile = new File(firstImage.getFilePath());
            config.put("firstImageFileExists", actualFile.exists());
        }
        
        log.info("=== API 기반 스트리밍 설정 테스트 결과 ===");
        config.forEach((key, value) -> log.info("{}: {}", key, value));
        
        return responseFactory.success("API 기반 스트리밍 설정 테스트 완료", config);
    }
    
    @GetMapping("/streaming-url/{imageId}")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> testStreamingUrl(@PathVariable Long imageId) {
        Map<String, Object> result = new HashMap<>();
        
        // DB에서 이미지 조회
        Image image = imageRepository.findById(imageId).orElse(null);
        if (image == null) {
            result.put("error", "이미지를 찾을 수 없습니다.");
            return responseFactory.success("이미지 스트리밍 URL 테스트 완료", result);
        }
        
        // API 기반 스트리밍 URL 생성
        String streamingUrl = fileUploadService.getImageStreamUrl(imageId);
        result.put("imageId", imageId);
        result.put("originalFileName", image.getOriginalFileName());
        result.put("contentType", image.getContentType());
        result.put("uploadedBy", image.getUploadedBy());
        result.put("generatedStreamingUrl", streamingUrl);
        result.put("fullStreamingUrl", "http://localhost:7950" + streamingUrl);
        
        // 실제 파일 존재 여부 확인
        File actualFile = new File(image.getFilePath());
        result.put("fileExists", actualFile.exists());
        result.put("fileAbsolutePath", actualFile.getAbsolutePath());
        result.put("fileSize", actualFile.exists() ? actualFile.length() : 0);
        
        log.info("=== API 기반 스트리밍 URL 테스트 ===");
        result.forEach((key, value) -> log.info("{}: {}", key, value));
        
        return responseFactory.success("API 기반 스트리밍 URL 테스트 완료", result);
    }
}
