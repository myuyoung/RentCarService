package me.changwook.service.impl;

import lombok.RequiredArgsConstructor;
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
public class LocalFileStorageService {

    private final Path fileStorageLocation;

    public LocalFileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        // Path 객체로 변환
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            // 저장할 디렉토리가 없으면 생성
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String saveFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String uniqueFilename = createUniqueFilename(originalFilename);

        try{
            if(uniqueFilename.contains("..")){
                throw new RuntimeException("잘못된 파일 이름");
            }
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);

            // 파일을 targetLocation에 복사, 이미 파일이 존재하면 덮어쓰기
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 파일에 접근할 수 있는 URL 생성
            // 예: http://localhost:8080/media/xxxxxxxx-xxxx.jpg
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/media/")
                    .path(uniqueFilename)
                    .toUriString();

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + uniqueFilename + ". Please try again!", ex);
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
