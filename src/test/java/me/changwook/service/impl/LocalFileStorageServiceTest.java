package me.changwook.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;

public class LocalFileStorageServiceTest {

    @TempDir
    Path tempDir;

    private LocalFileStorageService localFileStorageService;
    private String uploadDir;

    @BeforeEach
    void setUp() {
        uploadDir = tempDir.toString();
        localFileStorageService = new LocalFileStorageService(uploadDir);
        
        // Mock ServletRequestAttributes 설정
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("localhost");
        request.setServerPort(8080);
        request.setContextPath("");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    @DisplayName("파일 저장 성공 테스트 - 이미지 파일")
    void saveFile_imageFile_success() {
        // Given
        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        // When
        String fileUrl = localFileStorageService.saveFile(imageFile);

        // Then
        assertThat(fileUrl).isNotNull();
        assertThat(fileUrl).contains("/media/");
        assertThat(fileUrl).contains(".jpg");
        
        // 실제 파일이 저장되었는지 확인
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        Path savedFile = Paths.get(uploadDir, fileName);
        assertThat(Files.exists(savedFile)).isTrue();
    }

    @Test
    @DisplayName("파일 저장 성공 테스트 - 비디오 파일")
    void saveFile_videoFile_success() {
        // Given
        MockMultipartFile videoFile = new MockMultipartFile(
                "file",
                "test-video.mp4",
                "video/mp4",
                "test video content".getBytes()
        );

        // When
        String fileUrl = localFileStorageService.saveFile(videoFile);

        // Then
        assertThat(fileUrl).isNotNull();
        assertThat(fileUrl).contains("/media/");
        assertThat(fileUrl).contains(".mp4");
        
        // 실제 파일이 저장되었는지 확인
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        Path savedFile = Paths.get(uploadDir, fileName);
        assertThat(Files.exists(savedFile)).isTrue();
    }

    @Test
    @DisplayName("파일 저장 성공 테스트 - PNG 이미지")
    void saveFile_pngImage_success() {
        // Given
        MockMultipartFile pngFile = new MockMultipartFile(
                "file",
                "test-image.png",
                "image/png",
                "test png content".getBytes()
        );

        // When
        String fileUrl = localFileStorageService.saveFile(pngFile);

        // Then
        assertThat(fileUrl).isNotNull();
        assertThat(fileUrl).contains("/media/");
        assertThat(fileUrl).contains(".png");
    }

    @Test
    @DisplayName("잘못된 파일명 처리 테스트")
    void saveFile_maliciousFileName_throwsException() {
        // Given - 실제로 .. 경로가 포함된 파일명
        MockMultipartFile maliciousFile = new MockMultipartFile(
                "file",
                "test..file.jpg",  // .. 포함된 파일명
                "image/jpeg",
                "malicious content".getBytes()
        );

        // When & Then
        assertThatThrownBy(() -> localFileStorageService.saveFile(maliciousFile))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("잘못된 파일 이름");
    }

    @Test
    @DisplayName("확장자 없는 파일 처리 테스트")
    void saveFile_noExtension_success() {
        // Given
        MockMultipartFile noExtFile = new MockMultipartFile(
                "file",
                "testfile",
                "text/plain",
                "test content".getBytes()
        );

        // When
        String fileUrl = localFileStorageService.saveFile(noExtFile);

        // Then
        assertThat(fileUrl).isNotNull();
        assertThat(fileUrl).contains("/media/");
        // 확장자가 없으면 파일명 자체가 확장자로 인식됨
        assertThat(fileUrl).contains(".testfile");
    }

    @Test
    @DisplayName("같은 파일명으로 여러 번 업로드 시 고유한 파일명 생성 테스트")
    void saveFile_sameFileName_generatesUniqueNames() {
        // Given
        MockMultipartFile file1 = new MockMultipartFile(
                "file", "same-name.jpg", "image/jpeg", "content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile(
                "file", "same-name.jpg", "image/jpeg", "content2".getBytes());

        // When
        String url1 = localFileStorageService.saveFile(file1);
        String url2 = localFileStorageService.saveFile(file2);

        // Then
        assertThat(url1).isNotEqualTo(url2);
        assertThat(url1).contains(".jpg");
        assertThat(url2).contains(".jpg");
        
        // 두 파일 모두 실제로 저장되었는지 확인
        String fileName1 = url1.substring(url1.lastIndexOf("/") + 1);
        String fileName2 = url2.substring(url2.lastIndexOf("/") + 1);
        
        Path savedFile1 = Paths.get(uploadDir, fileName1);
        Path savedFile2 = Paths.get(uploadDir, fileName2);
        
        assertThat(Files.exists(savedFile1)).isTrue();
        assertThat(Files.exists(savedFile2)).isTrue();
    }

    @Test
    @DisplayName("빈 파일 처리 테스트")
    void saveFile_emptyFile_success() {
        // Given
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.txt",
                "text/plain",
                new byte[0]
        );

        // When
        String fileUrl = localFileStorageService.saveFile(emptyFile);

        // Then
        assertThat(fileUrl).isNotNull();
        assertThat(fileUrl).contains("/media/");
        assertThat(fileUrl).contains(".txt");
        
        // 빈 파일도 저장되었는지 확인
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        Path savedFile = Paths.get(uploadDir, fileName);
        assertThat(Files.exists(savedFile)).isTrue();
        
        try {
            assertThat(Files.size(savedFile)).isEqualTo(0);
        } catch (IOException e) {
            fail("파일 크기 확인 중 오류 발생", e);
        }
    }

    @Test
    @DisplayName("대용량 파일 처리 테스트")
    void saveFile_largeFile_success() {
        // Given - 1MB 크기의 파일 생성
        byte[] largeContent = new byte[1024 * 1024]; // 1MB
        for (int i = 0; i < largeContent.length; i++) {
            largeContent[i] = (byte) (i % 256);
        }
        
        MockMultipartFile largeFile = new MockMultipartFile(
                "file",
                "large-file.dat",
                "application/octet-stream",
                largeContent
        );

        // When
        String fileUrl = localFileStorageService.saveFile(largeFile);

        // Then
        assertThat(fileUrl).isNotNull();
        assertThat(fileUrl).contains("/media/");
        assertThat(fileUrl).contains(".dat");
        
        // 파일이 올바른 크기로 저장되었는지 확인
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        Path savedFile = Paths.get(uploadDir, fileName);
        assertThat(Files.exists(savedFile)).isTrue();
        
        try {
            assertThat(Files.size(savedFile)).isEqualTo(1024 * 1024);
        } catch (IOException e) {
            fail("파일 크기 확인 중 오류 발생", e);
        }
    }
}