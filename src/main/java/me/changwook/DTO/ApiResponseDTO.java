package me.changwook.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ApiResponseDTO<T> {
    private Boolean success;
    private String message;
    private T data;

    /**
     * 성공 응답 생성 (정적 팩토리 메소드)
     */
    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return ApiResponseDTO.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 성공 응답 생성 (데이터 없음)
     */
    public static ApiResponseDTO<Void> success(String message) {
        return success(message, null);
    }

    /**
     * 실패 응답 생성
     */
    public static <T> ApiResponseDTO<T> error(String message) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
