package me.changwook.DTO;

import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Boolean licence = false;

    @NotNull
    private String email;

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.licence = member.getLicence();
        this.email = member.getEmail();
    }
}
