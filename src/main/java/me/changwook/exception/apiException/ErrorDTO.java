package me.changwook.exception.apiException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorDTO  {

    private String message;
    private String code;


}
