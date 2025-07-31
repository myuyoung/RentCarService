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
}
