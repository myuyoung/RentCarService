package me.changwook.service;

import lombok.RequiredArgsConstructor;
import me.changwook.DTO.MemberDTO;
import me.changwook.domain.Member;
import me.changwook.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberDTO read(Long id) {
        Member member = memberRepository.findOne(id);
        return new MemberDTO(member);
    }

    @Transactional
    public void create(MemberDTO memberDTO) {
        Member member = Member.builder().name(memberDTO.getName()).email(memberDTO.getEmail()).licence(memberDTO.getLicence()).build();
        memberRepository.save(member);
    }

    @Transactional
    public void update(MemberDTO memberDTO) {
        Member member = memberRepository.findOne(memberDTO.getId());
        member.updateMember(memberDTO);
    }
}
