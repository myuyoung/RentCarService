package me.changwook.mapper;

import me.changwook.DTO.MemberDTO;
import me.changwook.domain.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDTO memberToMemberDTO(Member member);

    List<MemberDTO> membersToMemberDTOs(List<Member> members);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    Member memberDTOToMember(MemberDTO memberDTO);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "member_id", ignore = true)
    List<Member> memberDTOsToMembers(List<MemberDTO> memberDTOs);

}
