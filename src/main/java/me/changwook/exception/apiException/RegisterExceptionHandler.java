package me.changwook.exception.apiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.DTO.ErrorDTO;
import me.changwook.exception.custom.RegisterException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class RegisterExceptionHandler {

    private final MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RegisterException.class)
    public ErrorDTO RegisterException(RegisterException ex) {
      log.error("Exception Handler{}",ex.getMessage());
      return new ErrorDTO("BAD_REQUEST", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Map<String,String>>> ValidException (MethodArgumentNotValidException ex,Locale locale) {
        Map<String,String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            // 메시지 코드 (messages_ko.properties 키) → 메시지 값
            String message = messageSource.getMessage(error, locale);
            errors.put(error.getField(), message);
        }
        log.error("Exception Handler{}",errors);
        return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false,"로그인이 실패했습니다",errors));
    }
}
