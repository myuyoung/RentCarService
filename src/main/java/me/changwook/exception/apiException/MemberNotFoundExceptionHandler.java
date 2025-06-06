package me.changwook.exception.apiException;

import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.exception.custom.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class MemberNotFoundExceptionHandler{

    private static final String MESSAGE = "회원을 찾을 수 없습니다.";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> MemberNotFoundException() {

        log.error("HttpStatus = {}",HttpStatus.NOT_FOUND.name());

        ApiResponseDTO<Void> errorDTO = new ApiResponseDTO<>(false,MESSAGE,null);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }
}
