package me.changwook.service.impl;

import lombok.RequiredArgsConstructor;
import me.changwook.DTO.RegisterMemberDTO;
import me.changwook.domain.Member;
import me.changwook.exception.custom.RegisterException;
import me.changwook.mapper.RegisterMapper;
import me.changwook.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RegisterService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerMember(RegisterMemberDTO registerMemberDTO) {

        if(memberRepository.findByEmail(registerMemberDTO.getEmail()).isPresent()){
            throw new RegisterException("이미 존재하는 이메일입니다.");
        }

        registerMemberDTO.setPassword(passwordEncoder.encode(registerMemberDTO.getPassword()));
        Member member = new RegisterMapper().toDto(registerMemberDTO);

        memberRepository.save(member);
    }

}
