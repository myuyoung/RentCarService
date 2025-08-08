package me.changwook.controller;

import lombok.RequiredArgsConstructor;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.util.ResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

/**
 * 모든 컨트롤러의 기본 클래스
 * - 표준화된 응답 형식 제공
 * - ResponseFactory 공통 사용
 * - 공통 유틸리티 메서드 제공
 * 
 * 사용법:
 * 1. 컨트롤러 클래스에서 BaseController를 상속받습니다.
 * 2. ResponseFactory의 메서드들을 사용하여 일관된 응답을 생성합니다.
 * 
 * 예시:
 * ```java
 * @RestController
 * public class MyController extends BaseController {
 *     
 *     @GetMapping("/example")
 *     public ResponseEntity<ApiResponseDTO<String>> example() {
 *         return success("성공", "데이터");
 *     }
 * }
 * ```
 */
@RequiredArgsConstructor
public abstract class BaseController {

    @Autowired
    protected ResponseFactory responseFactory;

    // === 성공 응답 메서드들 ===

    /**
     * 200 OK 응답 (데이터 포함)
     */
    protected <T> ResponseEntity<ApiResponseDTO<T>> success(String message, T data) {
        return responseFactory.success(message, data);
    }

    /**
     * 200 OK 응답 (데이터 없음)
     */
    protected ResponseEntity<ApiResponseDTO<Void>> success(String message) {
        return responseFactory.success(message);
    }

    /**
     * 201 Created 응답 (데이터 포함)
     */
    protected <T> ResponseEntity<ApiResponseDTO<T>> created(String message, T data) {
        return responseFactory.created(message, data);
    }

    /**
     * 201 Created 응답 (데이터 없음)
     */
    protected ResponseEntity<ApiResponseDTO<Void>> created(String message) {
        return responseFactory.created(message);
    }

    /**
     * 202 Accepted 응답 (데이터 포함)
     */
    protected <T> ResponseEntity<ApiResponseDTO<T>> accepted(String message, T data) {
        return responseFactory.accepted(message, data);
    }

    /**
     * 202 Accepted 응답 (데이터 없음)
     */
    protected ResponseEntity<ApiResponseDTO<Void>> accepted(String message) {
        return responseFactory.accepted(message);
    }

    // === 실패 응답 메서드들 ===

    /**
     * 400 Bad Request 응답
     */
    protected <T> ResponseEntity<ApiResponseDTO<T>> badRequest(String message) {
        return responseFactory.badRequest(message);
    }

    /**
     * 401 Unauthorized 응답
     */
    protected <T> ResponseEntity<ApiResponseDTO<T>> unauthorized(String message) {
        return responseFactory.unauthorized(message);
    }

    /**
     * 403 Forbidden 응답
     */
    protected <T> ResponseEntity<ApiResponseDTO<T>> forbidden(String message) {
        return responseFactory.forbidden(message);
    }

    /**
     * 404 Not Found 응답
     */
    protected <T> ResponseEntity<ApiResponseDTO<T>> notFound(String message) {
        return responseFactory.notFound(message);
    }

    /**
     * 409 Conflict 응답
     */
    protected <T> ResponseEntity<ApiResponseDTO<T>> conflict(String message) {
        return responseFactory.conflict(message);
    }

    /**
     * 500 Internal Server Error 응답
     */
    protected <T> ResponseEntity<ApiResponseDTO<T>> internalServerError(String message) {
        return responseFactory.internalServerError(message);
    }
}