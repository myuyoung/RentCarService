package me.changwook.service.impl;

import me.changwook.domain.Image;
import me.changwook.domain.Member;
import me.changwook.repository.ImageRepository;
import me.changwook.repository.CarRegistrationSubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 【 계층 역할 】
 * - 파일 업로드/삭제/관리에 관한 핵심 비즈니스 로직 처리
 * - 파일 유효성 검증 및 보안 정책 적용
 * - 파일 시스템 조작 및 데이터베이스 연동
 * - 트랜잭션 관리 및 예외 처리
 * 
 * 【 비즈니스 규칙 】
 * - 파일 크기 제한: 10MB 이하
 * - 허용 형식: JPG, PNG, GIF, WEBP
 * - 파일명 중복 방지: UUID 기반 고유 파일명 생성
 * - 디렉터리 구조: yyyy/MM/dd 날짜별 분류
 * 
 * 【 온프레미스 파일 저장 정책 】
 * - 기본 경로: ${user.home}/uploads/images
 * - 파일 구조: /uploads/images/2024/01/15/uuid.jpg
 * - 메타데이터: 데이터베이스에 파일 정보 저장
 * 
 * @author changwook
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileUploadService {

    private final MemberRepository memberRepository;
    /**
     * 파일 업로드 기본 디렉터리 경로
     * application.yml: file.upload.dir
     */
    @Value("${file.upload.dir}")
    private String uploadDir;

    /**
     * 웹에서 접근 가능한 URL 경로
     * application.yml: file.upload.url-path
     */
    @Value("${file.upload.url-path}")
    private String urlPath;

    private final ImageRepository imageRepository;
    private final CarRegistrationSubmissionRepository submissionRepository;
    
    /**
     * 【 비즈니스 계층 】이미지 파일 업로드 핵심 비즈니스 로직
     * <p>
     * 담당 업무:
     * 1. 파일 유효성 검증 (크기, 형식, 보안)
     * 2. 온프레미스 파일 시스템에 물리적 파일 저장
     * 3. 고유한 파일명 생성으로 중복 방지
     * 4. 날짜별 디렉터리 구조 생성 및 관리
     * 5. 데이터베이스에 파일 메타데이터 저장
     * 6. 트랜잭션 무결성 보장
     * <p>
     * 비즈니스 규칙:
     * - 파일 크기 제한: 10MB
     * - 허용 형식: image/jpeg, image/png, image/gif, image/webp
     * - UUID 기반 고유 파일명으로 중복 방지
     * - 날짜별 디렉터리 구조로 관리 효율성 향상
     *
     * @param file       업로드할 파일 객체
     * @param uploadedBy 업로드한 사용자 식별자
     * @throws IOException 파일 I/O 오류 발생 시
     */
    public void uploadImage(MultipartFile file, String uploadedBy, UUID memberId, UUID submissionId) throws IOException {
        
        // 비즈니스 규칙 검증
        validateFile(file);

        // 온프레미스 저장소 디렉터리 준비
        String resolvedUploadDir = uploadDir.replace("${user.home}", System.getProperty("user.home"));
        String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Path uploadPath = Paths.get(resolvedUploadDir, dateDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath); // 날짜별 디렉터리 생성
        }
        
        log.info("=== 파일 업로드 경로 정보 ===");
        log.info("원본 uploadDir: {}", uploadDir);
        log.info("해결된 uploadDir: {}", resolvedUploadDir);
        log.info("날짜 디렉터리: {}", dateDir);
        log.info("최종 업로드 경로: {}", uploadPath);

        // 고유 파일명 생성 (중복 방지)
        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String storedFileName = UUID.randomUUID().toString() + fileExtension;
        String relativePath = (dateDir + "/" + storedFileName).trim();

        // 파일 시스템에 물리적 저장
        Path filePath = uploadPath.resolve(storedFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("파일 업로드 완료: {} -> {}", originalFileName, filePath.toString());

        // Image 엔티티 생성 (소유자 정보 제외)
        Image image = Image.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .filePath(filePath.toString())
                .relativePath(relativePath)
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .uploadedBy(uploadedBy)
                .build();

        if (submissionId != null) {
            // 제출건에 이미지 연결 (객체 연관으로 연결)
            var submission = submissionRepository.findById(submissionId)
                    .orElseThrow(() -> new IllegalArgumentException("제출 건을 찾을 수 없습니다."));
            image.setSubmission(submission);
            imageRepository.save(image);
        } else {
            // Member를 조회하여 Image 리스트에 추가 (memberId가 없으면 이메일로 조회)
            Member member;
            if (memberId != null) {
                member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
            } else {
                member = memberRepository.findByEmail(uploadedBy)
                        .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
            }
            image.setMember(member);
            imageRepository.save(image);
        }
    }

    /**
     * 【 비즈니스 계층 】이미지 파일 삭제 비즈니스 로직
     * 
     * 담당 업무:
     * 1. 이미지 메타데이터 존재성 검증
     * 2. 파일 시스템에서 물리적 파일 삭제
     * 3. 데이터베이스에서 메타데이터 삭제
     * 4. 삭제 과정의 트랜잭션 무결성 보장
     * 
     * 삭제 순서:
     * 1. DB에서 이미지 정보 조회
     * 2. 물리적 파일 삭제
     * 3. DB 메타데이터 삭제
     * 
     * @param imageId 삭제할 이미지의 고유 식별자
     * @return 삭제 성공 여부
     */
    public boolean deleteImage(Long imageId) {
        try {
            // 데이터 계층 호출: 이미지 정보 조회
            Image image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다."));

            // 파일 시스템에서 물리적 파일 삭제
            Path filePath = Paths.get(image.getFilePath());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("물리적 파일 삭제 완료: {}", filePath.toString());
            } else {
                log.warn(" 물리적 파일이 존재하지 않음: {}", filePath.toString());
            }

            // 데이터 계층 호출: 메타데이터 삭제
            imageRepository.delete(image);
            log.info("이미지 메타데이터 삭제 완료: ID={}", imageId);
            
            return true;
        } catch (Exception e) {
            log.error("파일 삭제 실패 (ID: {}): {}", imageId, e.getMessage());
            // 비즈니스 예외를 상위 계층에서 처리할 수 있도록 false 반환
            return false;
        }
    }

    /**
     * 【 비즈니스 계층 】이미지 URL 생성 비즈니스 로직
     * 
     * 담당 업무:
     * - 저장된 파일명을 웹에서 접근 가능한 URL로 변환
     * - WebConfig의 정적 리소스 설정과 연동
     * 
     * URL 구조: /images/{relativePath}
     * 예시: /images/2025/08/13/550e8400-e29b-41d4-a716-446655440000.jpg
     * 
     * @param pathSegment relativePath (날짜 구조 포함된 경로) 또는 단일 파일명
     * @return 웹에서 접근 가능한 상대 URL
     */
    public String getImageUrl(String pathSegment) {
        if (pathSegment == null || pathSegment.isEmpty()) {
            return null;
        }
        // pathSegment가 relativePath (e.g., yyyy/MM/dd/uuid.ext) 또는 단일 파일명
        // 슬래시로 시작하지 않도록 정리하고 개행문자 제거
        String cleanPath = pathSegment.trim().startsWith("/") ? pathSegment.trim().substring(1) : pathSegment.trim();
        return urlPath + "/" + cleanPath;
    }

    /**
     * 절대 파일 경로를 정적 서빙 URL로 변환합니다.
     * - 예: absolute "/home/user/uploads/images/2024/01/15/uuid.jpg"
     *   -> "/images/2024/01/15/uuid.jpg"
     */
    public String getImageUrlFromAbsolute(String absolutePath) {
        if (absolutePath == null) return null;
        String resolvedUploadDir = uploadDir.replace("${user.home}", System.getProperty("user.home"));
        String normalizedUploadDir = resolvedUploadDir.endsWith("/") ? resolvedUploadDir : resolvedUploadDir + "/";
        String rel = absolutePath.trim().startsWith(normalizedUploadDir)
                ? absolutePath.trim().substring(normalizedUploadDir.length())
                : absolutePath.trim();
        rel = rel.replace('\\', '/').trim();
        
        log.debug("=== 이미지 URL 변환 ===");
        log.debug("절대 경로: {}", absolutePath);
        log.debug("정규화된 업로드 디렉터리: {}", normalizedUploadDir);
        log.debug("상대 경로: {}", rel);
        log.debug("최종 URL: {}", getImageUrl(rel));
        
        return getImageUrl(rel);
    }
    
    /**
     * 【 비즈니스 계층 】파일 업로드 비즈니스 규칙 검증
     * 
     * 검증 항목:
     * 1. 파일 존재성 확인
     * 2. 이미지 파일 형식 검증
     * 3. 허용된 MIME 타입 확인
     * 4. 파일 크기 제한 검사
     * 
     * 비즈니스 제약사항:
     * - 빈 파일 업로드 금지
     * - 이미지 형식만 허용 (JPG, PNG, GIF, WEBP)
     * - 최대 파일 크기: 10MB
     * 
     * @param file 검증할 파일 객체
     * @throws IllegalArgumentException 비즈니스 규칙 위반 시
     */
    private void validateFile(MultipartFile file) {
        // 파일 존재성 검증
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        // 이미지 형식 기본 검증
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }

        // 허용된 이미지 형식 세부 검증
        String[] allowedTypes = {"image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"};
        boolean isAllowedType = false;
        for (String allowedType : allowedTypes) {
            if (allowedType.equals(contentType)) {
                isAllowedType = true;
                break;
            }
        }

        if (!isAllowedType) {
            throw new IllegalArgumentException("지원하지 않는 이미지 형식입니다. (jpg, png, gif, webp만 가능)");
        }

        // 파일 크기 제한 검증 (10MB = 10 * 1024 * 1024 bytes)
        final long MAX_FILE_SIZE = 10 * 1024 * 1024;
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기가 10MB를 초과할 수 없습니다.");
        }
        
        log.debug(" 파일 유효성 검증 통과: {} ({}bytes, {})",
                  file.getOriginalFilename(), file.getSize(), contentType);
    }

    /**
     * 【 비즈니스 계층 】파일 확장자 추출 유틸리티
     * 
     * 담당 업무:
     * - 원본 파일명에서 확장자 추출
     * - UUID 기반 파일명 생성 시 사용
     * 
     * 처리 로직:
     * - 확장자가 없는 경우 빈 문자열 반환
     * - 마지막 점(.) 이후 문자열을 확장자로 간주
     * 
     * @param fileName 원본 파일명
     * @return 확장자 (점 포함, 예: ".jpg")
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}