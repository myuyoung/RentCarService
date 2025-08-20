package me.changwook.util;

import me.changwook.DTO.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * API 응답 객체 생성을 위한 팩토리 클래스
 * - 일관된 응답 형식 제공
 * - 코드 중복 제거
 * - 응답 생성 로직 중앙화
 */
@Component
public class ResponseFactory {

    /**
     * 성공 응답 생성 (200 OK)
     */
    public <T> ResponseEntity<ApiResponseDTO<T>> success(String message, T data) {
        return ResponseEntity.ok(
            ApiResponseDTO.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build()
        );
    }

    /**
     * 성공 응답 생성 (200 OK, 데이터 없음)
     */
    public ResponseEntity<ApiResponseDTO<Void>> success(String message) {
        return success(message, null);
    }

    /**
     * 성공 응답 생성 (201 Created)
     */
    public <T> ResponseEntity<ApiResponseDTO<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponseDTO.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build()
        );
    }

    /**
     * 성공 응답 생성 (201 Created, 데이터 없음)
     */
    public ResponseEntity<ApiResponseDTO<Void>> created(String message) {
        return created(message, null);
    }

    /**
     * 성공 응답 생성 (202 Accepted)
     */
    public <T> ResponseEntity<ApiResponseDTO<T>> accepted(String message, T data) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
            ApiResponseDTO.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build()
        );
    }

    /**
     * 성공 응답 생성 (202 Accepted, 데이터 없음)
     */
    public ResponseEntity<ApiResponseDTO<Void>> accepted(String message) {
        return accepted(message, null);
    }

    /**
     * 실패 응답 생성
     */
    public <T> ResponseEntity<ApiResponseDTO<T>> error(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(
            ApiResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build()
        );
    }

    /**
     * 실패 응답 생성 (400 Bad Request)
     */
    public <T> ResponseEntity<ApiResponseDTO<T>> badRequest(String message) {
        return error(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * 실패 응답 생성 (401 Unauthorized)
     */
    public <T> ResponseEntity<ApiResponseDTO<T>> unauthorized(String message) {
        return error(message, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 실패 응답 생성 (403 Forbidden)
     */
    public <T> ResponseEntity<ApiResponseDTO<T>> forbidden(String message) {
        return error(message, HttpStatus.FORBIDDEN);
    }

    /**
     * 실패 응답 생성 (404 Not Found)
     */
    public <T> ResponseEntity<ApiResponseDTO<T>> notFound(String message) {
        return error(message, HttpStatus.NOT_FOUND);
    }

    /**
     * 실패 응답 생성 (405 Method Not Allowed)
     */
    public <T> ResponseEntity<ApiResponseDTO<T>> methodNotAllowed(String message) {
        return error(message, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 실패 응답 생성 (409 Conflict)
     */
    public <T> ResponseEntity<ApiResponseDTO<T>> conflict(String message) {
        return error(message, HttpStatus.CONFLICT);
    }

    /**
     * 실패 응답 생성 (422 Unprocessable Entity)
     */
    public <T> ResponseEntity<ApiResponseDTO<T>> unprocessableEntity(String message) {
        return error(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * 실패 응답 생성 (500 Internal Server Error)
     */
    public <T> ResponseEntity<ApiResponseDTO<T>> internalServerError(String message) {
        return error(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 실패 응답 생성 (503 Service Unavailable)
     */
    public <T> ResponseEntity<ApiResponseDTO<T>> serviceUnavailable(String message) {
        return error(message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}