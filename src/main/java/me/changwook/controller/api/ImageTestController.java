package me.changwook.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.service.impl.FileUploadService;
import me.changwook.util.ResponseFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 이미지 서빙 테스트를 위한 임시 컨트롤러
 * 관리자 페이지 이미지 문제 해결 후 삭제 예정
 */
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class ImageTestController {
    
    private final FileUploadService fileUploadService;
    private final ResponseFactory responseFactory;
    
    @Value("${file.upload.dir}")
    private String uploadDir;
    
    @Value("${file.upload.url-path}")
    private String urlPath;
    
    @GetMapping("/image-config")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> testImageConfig() {
        String resolvedUploadDir = uploadDir.replace("${user.home}", System.getProperty("user.home"));
        
        Map<String, Object> config = new HashMap<>();
        config.put("originalUploadDir", uploadDir);
        config.put("resolvedUploadDir", resolvedUploadDir);
        config.put("urlPath", urlPath);
        config.put("userHome", System.getProperty("user.home"));
        
        // 디렉터리 존재 여부 확인
        File uploadDirFile = new File(resolvedUploadDir);
        config.put("uploadDirExists", uploadDirFile.exists());
        config.put("uploadDirIsDirectory", uploadDirFile.isDirectory());
        
        // 2025/08/13 디렉터리 확인
        File dateDir = new File(resolvedUploadDir, "2025/08/13");
        config.put("dateDirExists", dateDir.exists());
        config.put("dateDirPath", dateDir.getAbsolutePath());
        
        if (dateDir.exists()) {
            File[] files = dateDir.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".jpg") || 
                name.toLowerCase().endsWith(".png") || 
                name.toLowerCase().endsWith(".gif") || 
                name.toLowerCase().endsWith(".webp"));
            config.put("imageFiles", files != null ? files.length : 0);
            if (files != null && files.length > 0) {
                config.put("firstImageFile", files[0].getName());
                // 첫 번째 이미지의 URL 생성 테스트
                String relativePath = "2025/08/13/" + files[0].getName();
                String generatedUrl = fileUploadService.getImageUrl(relativePath);
                config.put("generatedUrl", generatedUrl);
                config.put("fullUrl", "http://localhost:7950" + generatedUrl);
            }
        }
        
        log.info("=== 이미지 설정 테스트 결과 ===");
        config.forEach((key, value) -> log.info("{}: {}", key, value));
        
        return responseFactory.success("이미지 설정 테스트 완료", config);
    }
    
    @GetMapping("/image-url")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> testImageUrl(@RequestParam String relativePath) {
        Map<String, String> result = new HashMap<>();
        
        String generatedUrl = fileUploadService.getImageUrl(relativePath);
        result.put("inputRelativePath", relativePath);
        result.put("generatedUrl", generatedUrl);
        result.put("fullUrl", "http://localhost:7950" + generatedUrl);
        
        // 실제 파일 존재 여부 확인
        String resolvedUploadDir = uploadDir.replace("${user.home}", System.getProperty("user.home"));
        File actualFile = new File(resolvedUploadDir, relativePath);
        result.put("fileExists", String.valueOf(actualFile.exists()));
        result.put("fileAbsolutePath", actualFile.getAbsolutePath());
        
        log.info("=== 이미지 URL 테스트 ===");
        result.forEach((key, value) -> log.info("{}: {}", key, value));
        
        return responseFactory.success("이미지 URL 테스트 완료", result);
    }
}
