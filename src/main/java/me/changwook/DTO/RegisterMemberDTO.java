package me.changwook.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import me.changwook.domain.Member;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMemberDTO {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    //비밀번호가 영어로만 이루어져야 하고 8자 이상, 특수문자 하나 이상, 숫자가 하나 이상, 16자리 이하로 설정
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(?=.*[A-Za-z])[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,16}$")
    private String password;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;
}
