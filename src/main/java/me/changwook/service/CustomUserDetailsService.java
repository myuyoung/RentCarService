package me.changwook.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.changwook.DTO.MemberDTO;
import me.changwook.domain.Member;
import me.changwook.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByName(username);
        MemberDTO memberDTO  = new MemberDTO(member);
        return User.builder()
                .username(memberDTO.getName())
                .password("password")
                .authorities("ROLE_USER")
                .build();
    }
}
