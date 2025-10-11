package me.changwook.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class LocalFileStorageService {

    private final Path fileStorageLocation;

    public LocalFileStorageService(@Value("${file.upload.chat-dir}") String uploadDir) {
        // Path 객체로 변환
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            // 저장할 디렉토리가 없으면 생성
            Files.createDirectories(this.fileStorageLocation);
            log.info("파일 저장 디렉토리 초기화 완료: {}", this.fileStorageLocation);
        } catch (Exception ex) {
            log.error("파일 저장 디렉토리 생성 실패: {}", this.fileStorageLocation, ex);
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String saveFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        
        // 입력 검증
        if (file.isEmpty()) {
            log.error("빈 파일 업로드 시도");
            throw new RuntimeException("빈 파일을 업로드할 수 없습니다.");
        }
        
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            log.error("잘못된 파일명: null 또는 빈 문자열");
            throw new RuntimeException("잘못된 파일 이름입니다.");
        }
        
        String uniqueFilename = createUniqueFilename(originalFilename);
        log.info("파일 저장 시작: 원본명={}, 고유명={}, 크기={}", 
                originalFilename, uniqueFilename, file.getSize());

        try {
            if (uniqueFilename.contains("..")) {
                log.error("위험한 파일명 감지: {}", uniqueFilename);
                throw new RuntimeException("잘못된 파일 이름: 디렉토리 순회는 허용되지 않습니다.");
            }
            
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
            log.debug("파일 저장 대상 경로: {}", targetLocation);

            // 파일을 targetLocation에 복사, 이미 파일이 존재하면 덮어쓰기
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 파일에 접근할 수 있는 URL 생성
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/media/")
                    .path(uniqueFilename)
                    .toUriString();
            
            log.info("파일 저장 성공: 경로={}, URL={}", targetLocation, fileUrl);
            return fileUrl;

        } catch (IOException ex) {
            log.error("파일 저장 실패: 파일명={}, 오류={}", uniqueFilename, ex.getMessage());
            throw new RuntimeException("Could not store file " + uniqueFilename + ". Please try again!", ex);
        } catch (Exception ex) {
            log.error("파일 저장 중 예상치 못한 오류: 파일명={}", uniqueFilename, ex);
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다: " + uniqueFilename, ex);
        }
    }

    private String createUniqueFilename(String originalFilename) {
        return UUID.randomUUID().toString() + "." + extractExt(originalFilename);
    }

    private String extractExt(String originalFilename) {
        try {
            return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        } catch (StringIndexOutOfBoundsException e) {
            return "";
        }
    }
}
