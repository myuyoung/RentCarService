package me.changwook.configuration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * 【 통합 웹 설정 】
 * - API 기반 파일 스트리밍 (인증된 사용자만 접근 가능)
 * - 정적 리소스 매핑 (빠른 접근, 캐싱 지원)
 * 두 방식을 모두 지원하여 유연성 제공
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Value("${file.static-serving.enabled:true}")
    private boolean staticServingEnabled;

    @Value("${file.static-serving.cache-duration:3600}")
    private long cacheDurationSeconds;

    public WebConfig() {
        log.info("=== 통합 파일 서빙 모드로 실행 ===");
        log.info("API 기반 스트리밍: /api/files/view/{{imageId}} (인증 필요)");
        log.info("정적 리소스 매핑: /images/** (빠른 접근, 캐싱)");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!staticServingEnabled) {
            log.info("정적 리소스 서빙이 비활성화되었습니다. API 기반만 사용합니다.");
            return;
        }

        // 사용자 홈 디렉터리 경로 처리
        String resolvedUploadDir = uploadDir.replace("${user.home}", System.getProperty("user.home"));
        File uploadDirectory = new File(resolvedUploadDir);
        
        if (!uploadDirectory.exists()) {
            boolean created = uploadDirectory.mkdirs();
            log.info("업로드 디렉터리 생성: {} (성공: {})", resolvedUploadDir, created);
        }

        // 정적 리소스 핸들러 등록
        String resourcePath = "file:" + resolvedUploadDir + File.separator;
        
        registry.addResourceHandler("/images/**")
                .addResourceLocations(resourcePath)
                .setCacheControl(CacheControl.maxAge(cacheDurationSeconds, TimeUnit.SECONDS))
                .resourceChain(true);

        log.info("=== 정적 리소스 매핑 설정 ===");
        log.info("URL 패턴: /images/**");
        log.info("파일 위치: {}", resourcePath);
        log.info("캐시 지속시간: {}초", cacheDurationSeconds);
        log.info("원본 uploadDir: {}", uploadDir);
        log.info("해결된 uploadDir: {}", resolvedUploadDir);
    }
}


