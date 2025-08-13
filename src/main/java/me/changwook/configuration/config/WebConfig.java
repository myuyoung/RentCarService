package me.changwook.configuration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Value("${file.upload.url-path}")
    private String urlPath;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // 실제 경로로 변환 (${user.home} 등의 플레이스홀더 해결)
        String resolvedUploadDir = uploadDir.replace("${user.home}", System.getProperty("user.home"));
        String location = "file:" + (resolvedUploadDir.endsWith("/") ? resolvedUploadDir : resolvedUploadDir + "/");
        String pattern = (urlPath.endsWith("/**")) ? urlPath : (urlPath.endsWith("/")) ? urlPath + "**" : urlPath + "/**";
        
        log.info("=== 정적 리소스 매핑 설정 ===");
        log.info("URL 패턴: {}", pattern);
        log.info("파일 위치: {}", location);
        log.info("원본 uploadDir: {}", uploadDir);
        log.info("해결된 uploadDir: {}", resolvedUploadDir);
        
        registry.addResourceHandler(pattern)
                .addResourceLocations(location);
    }
}


