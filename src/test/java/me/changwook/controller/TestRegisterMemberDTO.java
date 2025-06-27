package me.changwook.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import me.changwook.DTO.RegisterMemberDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestRegisterMemberDTO extends RegisterMemberDTO {

    @NotBlank
    private String password;

    public TestRegisterMemberDTO(String name,String email,String password,String phone, String address){
        //부모의 필드를 초기화
        super(name, email, password, phone, address);
        this.password = password;
    }
}
