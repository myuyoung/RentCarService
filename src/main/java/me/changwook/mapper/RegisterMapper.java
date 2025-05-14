package me.changwook.mapper;

import me.changwook.DTO.RegisterMemberDTO;
import me.changwook.domain.Member;

public class RegisterMapper implements BasicMapper<Member,RegisterMemberDTO> {

    @Override
    public Member toDto(RegisterMemberDTO dto) {
        return Member.builder()
                .address(dto.getAddress())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .name(dto.getName())
                .build();
    }

    //회원가입시에 DTO객체로 변환할 필요가 없기 때문에 null로함.
    @Override
    public RegisterMemberDTO toEntity(Member entity) {
        return null;
    }
}
