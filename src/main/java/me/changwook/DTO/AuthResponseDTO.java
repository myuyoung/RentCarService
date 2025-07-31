package me.changwook.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.changwook.domain.Role;

@Getter
@AllArgsConstructor
@Builder
public class AuthResponseDTO {

    private String token;
    private String email;
    private String name;
    private Role role;

}
