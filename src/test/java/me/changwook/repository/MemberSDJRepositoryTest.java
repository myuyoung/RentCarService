package me.changwook.repository;

import me.changwook.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberSDJRepositoryTest {

    @Autowired
    private MemberSDJRepository memberSDJRepository;

    @Test
    void save() {
        Member member1 = Member.builder().name("욱").licence(true).build();
        Member savedMember = memberSDJRepository.save(member1);

        Assertions.assertThat(savedMember.getName()).isEqualTo(member1.getName());
    }
}