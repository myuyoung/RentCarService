package me.changwook.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.domain.Image;
import me.changwook.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 【 데이터 수정 서비스 】기존 이미지 데이터의 MIME 타입 수정
 * 
 * 목적:
 * - 기존에 업로드된 이미지의 잘못된 MIME 타입 수정
 * - "image/jpg" → "image/jpeg" 등 표준화
 * - JPG 이미지 표시 문제 해결
 * 
 * @author changwook
 * @since 1.1
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ImageMimeTypeFixService {

    private final ImageRepository imageRepository;

    /**
     * 모든 이미지의 MIME 타입을 표준화합니다.
     * 
     * 수행 작업:
     * 1. "image/jpg" → "image/jpeg" 변환
     * 2. 파일명 기반 MIME 타입 추론
     * 3. 데이터베이스 업데이트
     * 
     * @return 수정된 이미지 개수
     */
    @Transactional
    public int fixAllImageMimeTypes() {
        log.info("=== 이미지 MIME 타입 표준화 시작 ===");
        
        List<Image> allImages = imageRepository.findAll();
        int fixedCount = 0;
        
        for (Image image : allImages) {
            String originalContentType = image.getContentType();
            String fixedContentType = normalizeImageContentType(originalContentType, image.getOriginalFileName());
            
            if (!originalContentType.equals(fixedContentType)) {
                image.setContentType(fixedContentType);
                imageRepository.save(image);
                fixedCount++;
                
                log.info("MIME 타입 수정: {} (파일: {}) | {} → {}", 
                        image.getId(), image.getOriginalFileName(), originalContentType, fixedContentType);
            }
        }
        
        log.info("=== MIME 타입 표준화 완료: {}개 이미지 수정 ===", fixedCount);
        return fixedCount;
    }

    /**
     * 특정 이미지의 MIME 타입을 수정합니다.
     * 
     * @param imageId 수정할 이미지 ID
     * @return 수정 성공 여부
     */
    @Transactional
    public boolean fixImageMimeType(Long imageId) {
        try {
            Image image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다: " + imageId));
            
            String originalContentType = image.getContentType();
            String fixedContentType = normalizeImageContentType(originalContentType, image.getOriginalFileName());
            
            if (!originalContentType.equals(fixedContentType)) {
                image.setContentType(fixedContentType);
                imageRepository.save(image);
                
                log.info("단일 이미지 MIME 타입 수정 완료: {} | {} → {}", 
                        imageId, originalContentType, fixedContentType);
                return true;
            }
            
            return false; // 수정이 필요하지 않음
        } catch (Exception e) {
            log.error("이미지 MIME 타입 수정 실패: {}", imageId, e);
            return false;
        }
    }

    /**
     * MIME 타입 정규화 (FileUploadService와 동일한 로직)
     * 
     * @param contentType 원본 MIME 타입
     * @param originalFileName 파일명 (확장자 추론용)
     * @return 정규화된 MIME 타입
     */
    private String normalizeImageContentType(String contentType, String originalFileName) {
        if (contentType == null) {
            return inferContentTypeFromFileName(originalFileName);
        }
        
        String normalized = contentType.toLowerCase().trim();
        
        switch (normalized) {
            case "image/jpg":
                return "image/jpeg";
            case "image/jpeg":
            case "image/png":
            case "image/gif":
            case "image/webp":
                return normalized;
            default:
                // 알 수 없는 타입인 경우 파일명으로 추론
                String inferred = inferContentTypeFromFileName(originalFileName);
                if (inferred != null) {
                    log.warn("MIME 타입 추론: {} → {} (파일명: {})", contentType, inferred, originalFileName);
                    return inferred;
                }
                
                log.warn("알 수 없는 MIME 타입, 기본값 적용: {} → image/jpeg", contentType);
                return "image/jpeg";
        }
    }

    /**
     * 파일명 확장자로부터 MIME 타입 추론
     * 
     * @param originalFileName 원본 파일명
     * @return 추론된 MIME 타입
     */
    private String inferContentTypeFromFileName(String originalFileName) {
        if (originalFileName == null) {
            return "image/jpeg";
        }
        
        String extension = getFileExtension(originalFileName).toLowerCase();
        
        switch (extension) {
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
            case ".png":
                return "image/png";
            case ".gif":
                return "image/gif";
            case ".webp":
                return "image/webp";
            default:
                return "image/jpeg";
        }
    }

    /**
     * 파일 확장자 추출
     * 
     * @param fileName 파일명
     * @return 확장자 (점 포함)
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
