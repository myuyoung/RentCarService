package me.changwook.exception;

import me.changwook.common.ApiResponse;
import me.changwook.image.ImageController;
import me.changwook.member.UserImageController;
import me.changwook.common.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

/**
 * 이미지 업로드 전용 예외 처리 핸들러 (ResponseFactory 사용)
 * 이미지 업로드 관련 컨트롤러에서 발생하는 예외를 처리
 * - ImageController: 관리자용 이미지 업로드 (/api/admin/images)
 * - UserImageController: 사용자용 이미지 업로드 (/api/user/image)
 * 
 * 💡 적용 범위: assignableTypes로 특정 컨트롤러에만 적용
 * 💡 처리 예외: IOException, IllegalArgumentException, MaxUploadSizeExceededException 등
 */
@RestControllerAdvice(assignableTypes = {ImageController.class, UserImageController.class})
@Slf4j
@RequiredArgsConstructor
public class ImageUploadExceptionHandler {

    private final ResponseFactory responseFactory;

    /**
     * 파일 I/O 예외 처리
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<Void>> handleIOException(IOException e) {
        log.error("파일 I/O 오류 발생: ", e);
        return responseFactory.internalServerError("파일 처리 중 오류가 발생했습니다: " + e.getMessage());
    }

    /**
     * 잘못된 인자 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("잘못된 요청: ", e);
        return responseFactory.badRequest(e.getMessage());
    }

    /**
     * 파일 크기 초과 예외 처리
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.warn("파일 크기 초과: ", e);
        return responseFactory.badRequest("파일 크기가 허용 범위를 초과했습니다.");
    }

    /**
     * 일반적인 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception e) {
        log.error("예상치 못한 오류 발생: ", e);
        return responseFactory.internalServerError("서버 내부 오류가 발생했습니다.");
    }
}