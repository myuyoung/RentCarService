package me.changwook.exception.custom;

import me.changwook.DTO.ApiResponseDTO;

public class RegisterException extends RuntimeException {
    public RegisterException(String message) {
        super(message);
    }


}
