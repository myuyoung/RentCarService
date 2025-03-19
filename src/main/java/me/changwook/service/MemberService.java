package me.changwook.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.changwook.DTO.MemberDTO;
import me.changwook.DTO.RegisterMemberDTO;
import me.changwook.domain.Member;
import me.changwook.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MemberDTO findById(Long id) {
        Member member = memberRepository.findOne(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        return new MemberDTO(member);
    }

    @Transactional
    public void create(MemberDTO memberDTO) {
        Member member = Member.builder().name(memberDTO.getName()).email(memberDTO.getEmail()).licence(memberDTO.getLicence()).build();
        memberRepository.save(member);
    }

    @Transactional
    public void update(MemberDTO memberDTO) {
        Member member = memberRepository.findOne(memberDTO.getId()).orElseThrow();
        member.updateMember(memberDTO);
    }

    @Transactional
    public void registerMember(RegisterMemberDTO registerMemberDTO) {
        memberRepository.findByName(registerMemberDTO.getName()).ifPresent(m ->{throw new RuntimeException("이미 존재하는 회원입니다."); });

        Member member = Member.builder().email(registerMemberDTO.getEmail()).password(passwordEncoder.encode(registerMemberDTO.getPassword())).build();

        memberRepository.save(member);
    }


}
