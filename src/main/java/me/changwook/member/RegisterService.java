package me.changwook.member;

import lombok.RequiredArgsConstructor;
import me.changwook.member.dto.RegisterMemberDTO;
import me.changwook.exception.custom.RegisterException;
import me.changwook.mapper.RegisterMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RegisterService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegisterMapper registerMapper;

    @Transactional
    public void registerMember(RegisterMemberDTO registerMemberDTO) {

        if(memberRepository.findByEmail(registerMemberDTO.getEmail()).isPresent()){
            throw new RegisterException("이미 존재하는 이메일입니다.");
        }

        registerMemberDTO.setPassword(passwordEncoder.encode(registerMemberDTO.getPassword()));
        Member member = registerMapper.registerDTOToMember(registerMemberDTO);

        memberRepository.save(member);
    }

}
