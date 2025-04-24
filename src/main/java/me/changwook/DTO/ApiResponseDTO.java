package me.changwook.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponseDTO<T> {
    private Boolean success;
    private String message;
    private T data;
}
