package me.changwook.DTO;

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

    private Boolean licence = false;

    private String email;

    private String phone;

    private String address;

}
