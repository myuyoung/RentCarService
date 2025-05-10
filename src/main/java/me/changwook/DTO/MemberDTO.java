package me.changwook.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.changwook.domain.Member;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private Long id;

    private String name;

    private Boolean licence = false;

    private String email;

    private String phone;

    private String address;

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.licence = member.getLicence();
        this.email = member.getEmail();
    }
}
