package me.changwook.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import me.changwook.DTO.MemberDTO;
import me.changwook.domain.Member;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-24T05:46:51+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.jar, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public MemberDTO memberToMemberDTO(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberDTO.MemberDTOBuilder memberDTO = MemberDTO.builder();

        memberDTO.id( member.getId() );
        memberDTO.name( member.getName() );
        memberDTO.licence( member.getLicence() );
        memberDTO.email( member.getEmail() );
        memberDTO.phone( member.getPhone() );
        memberDTO.address( member.getAddress() );

        return memberDTO.build();
    }

    @Override
    public List<MemberDTO> membersToMemberDTOs(List<Member> members) {
        if ( members == null ) {
            return null;
        }

        List<MemberDTO> list = new ArrayList<MemberDTO>( members.size() );
        for ( Member member : members ) {
            list.add( memberToMemberDTO( member ) );
        }

        return list;
    }

    @Override
    public Member memberDTOToMember(MemberDTO memberDTO) {
        if ( memberDTO == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.name( memberDTO.getName() );
        member.licence( memberDTO.getLicence() );
        member.email( memberDTO.getEmail() );
        member.phone( memberDTO.getPhone() );
        member.address( memberDTO.getAddress() );

        return member.build();
    }

    @Override
    public List<Member> memberDTOsToMembers(List<MemberDTO> memberDTOs) {
        if ( memberDTOs == null ) {
            return null;
        }

        List<Member> list = new ArrayList<Member>( memberDTOs.size() );
        for ( MemberDTO memberDTO : memberDTOs ) {
            list.add( memberDTOToMember( memberDTO ) );
        }

        return list;
    }
}
