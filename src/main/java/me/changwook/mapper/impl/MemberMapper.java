package me.changwook.mapper.impl;

import me.changwook.DTO.MemberDTO;
import me.changwook.domain.Member;

public class MemberMapper {
    //엔티티객체로 바꾸는 로직
    public static Member toEntity(MemberDTO dto) {
        return Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .licence(dto.getLicence())
                .id(dto.getId())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
    }

    //DTO로 바꾸는 로직
    public static MemberDTO toDTO(Member entity) {
        return MemberDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .licence(entity.getLicence())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .build();
    }
}
