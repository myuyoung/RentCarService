package me.changwook.test_repository;

import me.changwook.configuration.config.QuerydslConfig;
import me.changwook.domain.Member;
import me.changwook.domain.RentCars;
import me.changwook.repository.MemberRepository;
import me.changwook.repository.RentCarsRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(QuerydslConfig.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RentCarsRepository rentCarsRepository;

    @Test
    void member(){
        Member member = Member.builder().licence(true).email("test@gmail.com").name("욱").build();
        memberRepository.save(member);

        assertThat(member.getLicence()).isTrue();
    }

    @Test
    void rentCars(){
        RentCars rentCars1 = RentCars.builder().rentPrice(50000).build();
        RentCars rentCars2 = RentCars.builder().rentPrice(20000).build();
        rentCarsRepository.save(rentCars1);
        rentCarsRepository.save(rentCars2);
        assertThat(rentCars1.getRentPrice()).isEqualTo(50000);

        List<RentCars> allByCarId = rentCarsRepository.findAllByCarId();
        assertThat(allByCarId).isNotEmpty();

    }
}