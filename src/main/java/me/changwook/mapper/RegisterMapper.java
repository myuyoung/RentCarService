package me.changwook.mapper;

import me.changwook.member.dto.RegisterMemberDTO;
import me.changwook.member.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RegisterMapper  {

    RegisterMapper INSTANCE = Mappers.getMapper(RegisterMapper.class);
    //memberToDTO 작성하기

    RegisterMemberDTO memberToRegisterDTO(Member member);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "licence", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "failedLoginAttempts", ignore = true)
    @Mapping(target = "accountLockedUntil", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "images", ignore = true)
    Member registerDTOToMember(RegisterMemberDTO registerMemberDTO);



}
