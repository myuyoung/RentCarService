package me.changwook.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorDTO  {

    private String message;
    private String code;


}
