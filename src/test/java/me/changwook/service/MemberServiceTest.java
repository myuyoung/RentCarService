package me.changwook.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.MemberDTO;
import me.changwook.domain.Member;
import me.changwook.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class MemberServiceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("회원 정보 수정 로직")
    @Transactional
    @Rollback
    void updateMember() {
        //given
        Member member = Member.builder().email("changwook@gmail.com").licence(true).name("changwook").build();
        memberRepository.save(member);
        entityManager.flush(); // DB 반영
        entityManager.clear(); // 1차 캐시 초기화
        log.info("member: {}", member.toString());

        //when
        Member one1 = memberRepository.findOne(member.getId()).orElseThrow();
        MemberDTO memberDTO = new MemberDTO(one1);
        memberDTO.setEmail("jjjonga33@naver.com");
        memberService.update(memberDTO);
        entityManager.flush();
        entityManager.clear();
        log.info("member.getEmail: {}", member.getEmail());
        //then
        Member one = memberRepository.findOne(member.getId()).orElseThrow();
        log.info("one: {}", one.toString());
        assertThat(one.getEmail()).isEqualTo("jjjonga33@naver.com");
    }

    @Test
    @Transactional
    void createMember(){
        //given
        Member member = Member.builder().email("jjjonga33@naver.com").licence(true).name("jjjonga33").build();
        memberRepository.save(member);
        entityManager.flush();
        entityManager.clear();
        //when
        Member findOne = memberRepository.findOne(member.getId()).orElseThrow();
        //then
        assertThat(findOne.getName()).isEqualTo(member.getName());
        log.info("findOne.getEmail: {}, findOne.getName:{} ", findOne.getEmail(), findOne.getName());
    }
}