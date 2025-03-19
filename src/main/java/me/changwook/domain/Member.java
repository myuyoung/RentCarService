package me.changwook.domain;

import jakarta.persistence.*;
import lombok.*;
import me.changwook.DTO.MemberDTO;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id",updatable = false)
    private Long id;

    private String name;

    @Column(nullable = false,columnDefinition = "BOOLEAN")
    private Boolean licence = false;

    private String email;

    private String password;

    @OneToOne(mappedBy = "member")
    private Rent rent;

    public void setMember(Rent rent){
        this.rent = rent;
    }

    //더티 체킹을 위한 메서드
    public void updateMember(MemberDTO memberDTO){
        this.id = memberDTO.getId();
        this.name = memberDTO.getName();
        this.licence = memberDTO.getLicence();
        this.email = memberDTO.getEmail();
    }
    public MemberDTO toDTO(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(this.id);
        memberDTO.setName(this.name);
        memberDTO.setLicence(this.licence);
        memberDTO.setEmail(this.email);
        return memberDTO;
    }
}
