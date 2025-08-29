package me.changwook.mapper;

import javax.annotation.processing.Generated;
import me.changwook.DTO.RegisterMemberDTO;
import me.changwook.domain.Member;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-29T17:19:38+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.jar, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class RegisterMapperImpl implements RegisterMapper {

    @Override
    public RegisterMemberDTO memberToRegisterDTO(Member member) {
        if ( member == null ) {
            return null;
        }

        RegisterMemberDTO.RegisterMemberDTOBuilder registerMemberDTO = RegisterMemberDTO.builder();

        registerMemberDTO.name( member.getName() );
        registerMemberDTO.email( member.getEmail() );
        registerMemberDTO.password( member.getPassword() );
        registerMemberDTO.phone( member.getPhone() );
        registerMemberDTO.address( member.getAddress() );

        return registerMemberDTO.build();
    }

    @Override
    public Member registerDTOToMember(RegisterMemberDTO registerMemberDTO) {
        if ( registerMemberDTO == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.name( registerMemberDTO.getName() );
        member.email( registerMemberDTO.getEmail() );
        member.phone( registerMemberDTO.getPhone() );
        member.address( registerMemberDTO.getAddress() );
        member.password( registerMemberDTO.getPassword() );

        return member.build();
    }
}
