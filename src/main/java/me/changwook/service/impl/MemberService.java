package me.changwook.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.changwook.DTO.MemberDTO;
import me.changwook.domain.Member;
import me.changwook.mapper.MemberMapper;
import me.changwook.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Transactional(readOnly = true)
    public MemberDTO findById(UUID id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        return memberMapper.memberToMemberDTO(member);
    }

    @Transactional
    public void update(MemberDTO memberDTO) {
        Member member = memberRepository.findById(memberDTO.getId()).orElseThrow(()-> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        //업데이트 할 메서드를 작성
        member.updateMember(memberMapper.memberDTOToMember(memberDTO));
    }

}
