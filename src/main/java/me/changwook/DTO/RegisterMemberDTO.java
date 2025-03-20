package me.changwook.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.changwook.domain.Member;

@Data
@RequiredArgsConstructor
public class RegisterMemberDTO {

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String password;

}
