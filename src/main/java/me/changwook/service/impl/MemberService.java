package me.changwook.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.changwook.DTO.MemberDTO;
import me.changwook.domain.Member;
import me.changwook.mapper.impl.MemberMapper;
import me.changwook.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;

    @Transactional(readOnly = true)
    public MemberDTO findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        return new MemberDTO(member);
    }

    @Transactional
    public void create(MemberDTO memberDTO) {
        Member member = Member.builder().name(memberDTO.getName()).email(memberDTO.getEmail()).licence(memberDTO.getLicence()).build();
        memberRepository.save(member);
    }

    @Transactional
    public MemberDTO inquiry(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        return memberMapper.memberToMemberDTO(member);
    }

    @Transactional
    public void update(MemberDTO memberDTO) {
        Member member = memberRepository.findById(memberDTO.getId()).orElseThrow();
        //업데이트 할 메서드를 작성
        member.updateMember(memberMapper.memberDTOToMember(memberDTO));
    }

}
