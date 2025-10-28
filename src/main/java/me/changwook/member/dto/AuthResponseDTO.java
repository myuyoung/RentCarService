package me.changwook.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.changwook.member.Role;

@Getter
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private String token;
    private String email;
    private String name;
    private Role role;
    private String redirectUrl;
}
