package me.changwook.mapper;

import me.changwook.DTO.MemberDTO;
import me.changwook.DTO.RegisterMemberDTO;
import me.changwook.domain.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RegisterMapper  {

    RegisterMapper INSTANCE = Mappers.getMapper(RegisterMapper.class);
    //memberToDTO 작성하기

    RegisterMemberDTO memberToRegisterDTO(Member member);

    Member registerDTOToMember(RegisterMemberDTO registerMemberDTO);



}
