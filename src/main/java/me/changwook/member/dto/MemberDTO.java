package me.changwook.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private UUID id;

    private String name;

    @Builder.Default
    private Boolean licence = false;

    private String email;

    private String phone;

    private String address;

}
