package me.changwook.exception;

import me.changwook.DTO.ApiResponseDTO;
import me.changwook.exception.custom.DuplicateRentCarException;
import me.changwook.exception.custom.MemberNotFoundException;
import me.changwook.exception.custom.RegisterException;
import me.changwook.exception.custom.RentCarNotFoundException;
import me.changwook.exception.custom.ReservationConflictException;
import me.changwook.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * 【 통합 예외 처리 핸들러 】
 * 
 * 전체 애플리케이션에서 발생하는 모든 예외를 처리합니다.
 * 
 * 담당 업무:
 * - 도메인별 커스텀 예외 처리
 * - 시스템 레벨 예외 처리  
 * - 검증 예외 처리
 * - 보안 예외 처리
 * - 데이터베이스 예외 처리
 * 
 * 💡 적용 범위: 전체 @RestController
 * 💡 응답 형식: 일관된 ApiResponseDTO 형태
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ResponseFactory responseFactory;

    // =============================================================
    // 도메인별 커스텀 예외 처리
    // =============================================================

    /**
     * 회원을 찾을 수 없을 때
     */
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleMemberNotFoundException(MemberNotFoundException e) {
        log.warn("회원을 찾을 수 없음: {}", e.getMessage());
        return responseFactory.notFound(e.getMessage());
    }

    /**
     * 렌트카를 찾을 수 없을 때
     */
    @ExceptionHandler(RentCarNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleRentCarNotFoundException(RentCarNotFoundException e) {
        log.warn("렌트카를 찾을 수 없음: {}", e.getMessage());
        return responseFactory.notFound(e.getMessage());
    }

    /**
     * 중복된 렌트카 등록 시도
     */
    @ExceptionHandler(DuplicateRentCarException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleDuplicateRentCarException(DuplicateRentCarException e) {
        log.warn("중복 렌트카 등록 시도: {}", e.getMessage());
        return responseFactory.conflict(e.getMessage());
    }

    /**
     * 회원 등록 예외 처리
     */
    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleRegisterException(RegisterException e) {
        log.warn("회원 등록 실패: {}", e.getMessage());
        return responseFactory.badRequest(e.getMessage());
    }

    /**
     * 예약 충돌 예외 처리
     */
    @ExceptionHandler(ReservationConflictException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleReservationConflictException(ReservationConflictException e) {
        log.warn("예약 충돌: {}", e.getMessage());
        return responseFactory.conflict(e.getMessage());
    }

    // =============================================================
    // 시스템 레벨 예외 처리
    // =============================================================

    /**
     * 잘못된 요청 파라미터 검증 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        log.warn("요청 파라미터 검증 실패: {}", errorMessage);
        return responseFactory.badRequest("입력값 검증 실패: " + errorMessage);
    }

    /**
     * 잘못된 JSON 형식
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("잘못된 JSON 형식: {}", e.getMessage());
        return responseFactory.badRequest("잘못된 요청 형식입니다.");
    }

    /**
     * 지원하지 않는 HTTP 메서드
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("지원하지 않는 HTTP 메서드: {}", e.getMethod());
        return responseFactory.methodNotAllowed("지원하지 않는 HTTP 메서드입니다: " + e.getMethod());
    }

    /**
     * 엔드포인트를 찾을 수 없음
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("엔드포인트를 찾을 수 없음: {} {}", e.getHttpMethod(), e.getRequestURL());
        return responseFactory.notFound("요청한 엔드포인트를 찾을 수 없습니다.");
    }

    /**
     * 정적 리소스를 찾을 수 없음 (favicon.ico 등)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleNoResourceFoundException(NoResourceFoundException e) {
        // favicon.ico 요청은 로그 출력하지 않음 (너무 빈번함)
        if (!e.getMessage().contains("favicon.ico")) {
            log.warn("정적 리소스를 찾을 수 없음: {}", e.getMessage());
        }
        return responseFactory.notFound("요청한 리소스를 찾을 수 없습니다.");
    }

    /**
     * 데이터베이스 무결성 제약 위반
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("데이터 무결성 제약 위반: ", e);
        return responseFactory.conflict("데이터 제약 조건을 위반했습니다.");
    }

    /**
     * JPA 엔티티를 찾을 수 없음
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("엔티티를 찾을 수 없음: {}", e.getMessage());
        return responseFactory.notFound("요청한 리소스를 찾을 수 없습니다.");
    }

    /**
     * 접근 권한 없음
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("접근 권한 없음: {}", e.getMessage());
        return responseFactory.forbidden("접근 권한이 없습니다.");
    }

    /**
     * 인증 실패 (로그인 정보 틀림)
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleBadCredentialsException(BadCredentialsException e) {
        log.warn("인증 실패: {}", e.getMessage());
        return responseFactory.unauthorized("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    // =============================================================
    // 파일 및 I/O 예외 처리
    // =============================================================

    /**
     * 파일 I/O 예외 처리
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleIOException(IOException e) {
        log.error("파일 I/O 오류 발생: ", e);
        return responseFactory.internalServerError("파일 처리 중 오류가 발생했습니다: " + e.getMessage());
    }

    /**
     * 파일 크기 초과 예외 처리
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.warn("파일 크기 초과: ", e);
        return responseFactory.badRequest("파일 크기가 허용 범위를 초과했습니다.");
    }

    // =============================================================
    // 범용 예외 처리
    // =============================================================

    /**
     * 잘못된 인자 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("잘못된 요청: ", e);
        return responseFactory.badRequest(e.getMessage());
    }

    /**
     * 일반적인 런타임 예외 처리
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleRuntimeException(RuntimeException e) {
        log.error("런타임 예외 발생: ", e);
        return responseFactory.internalServerError("처리 중 오류가 발생했습니다: " + e.getMessage());
    }

    /**
     * 예상치 못한 모든 예외의 최종 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleGenericException(Exception e) {
        log.error("예상치 못한 오류 발생: ", e);
        return responseFactory.internalServerError("서버 내부 오류가 발생했습니다.");
    }
}