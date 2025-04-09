package me.changwook.exception.apiException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorDTO handleBadRequest(IllegalArgumentException e) {
        log.error("Exception Handler{}",e.getMessage());
        return new ErrorDTO("BAD_REQUEST", e.getMessage());
    }

}
