package me.changwook.service;

import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.RentCarsDTO;
import me.changwook.DTO.RentDTO;
import me.changwook.DTO.ReservationDTO;
import me.changwook.domain.*;
import me.changwook.mapper.MemberMapper;
import me.changwook.mapper.RentCarsMapper;
import me.changwook.mapper.RentMapper;
import me.changwook.repository.CategoryRepository;
import me.changwook.repository.MemberRepository;
import me.changwook.repository.RentCarsRepository;
import me.changwook.repository.RentRepository;
import me.changwook.service.impl.RentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback
@Slf4j
public class RentServiceTests {

    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RentCarsRepository rentCarsRepository;
    @Autowired
    private RentService rentService;
    @Autowired
    private RentMapper rentMapper;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RentCarsMapper rentCarsMapper;
    @Autowired
    private MemberMapper memberMapper;

    @BeforeEach
    @Transactional
    void setUp() {
        Category category = categoryRepository.save(createTestCategory());

        RentCars rentCars = rentCarsRepository.save(createTestRentCars(category));

        rentCarsRepository.save(rentCars);

        if(!memberRepository.existsByEmail("guildong@email.com")){
            Member testMember = Member.builder()
                    .name("홍길동")
                    .email("guildong@email.com")
                    .password("password") // 테스트용이므로 간단하게 설정
                    .licence(true) // 운전면허 있음
                    .build();
            memberRepository.save(testMember);
        }
    }

    private Category createTestCategory() {
        return Category.builder()
                .fuelType(FuelType.GASOLINE)
                .rentCarsSegment(RentCarsSegment.MEDIUM)
                .build();
    }

    private RentCars createTestRentCars(Category category) {
        return RentCars.builder()
                .rentCarNumber("21가5231")
                .rentPrice(50000)
                .name("소나타")
                .recommend(50L)
                .totalDistance(35000)
                .category(category)
                .build();
    }

    private Rent createTestRent() {
        return Rent.builder()
                .endDate(LocalDate.now().plusDays(2).atStartOfDay())
                .duration(2)
                .rentDate(LocalDate.now().atStartOfDay())
                .build();
    }


    private ReservationDTO createTestReservationDTO() {

        RentCars rentCars = rentCarsRepository.findByRentCarNumber("21가5231")
                .orElseThrow(() -> new RuntimeException("테스트 렌트카를 찾을 수 없습니다"));

        // RentDTO 생성
        RentDTO rentDTO = RentDTO.builder()
                .rentTime(LocalDateTime.now())
                .endTime(LocalDate.now().plusDays(2).atStartOfDay())
                .duration(2)
                .build();

        // RentCarsDTO 생성
        RentCarsDTO rentCarsDTO = rentCarsMapper.rentCarsToRentCarsDTO(rentCars);

        // ReservationDTO 생성 및 설정
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setRentDTO(rentDTO);
        reservationDTO.setRentCarsDTO(rentCarsDTO);

        return reservationDTO;

    }

    @Test
    @DisplayName("렌트카 예약 저장")
    void reservationTest() {
        ReservationDTO reservationDTO = createTestReservationDTO();

        Member member = memberRepository.findByEmail("guildong@email.com").orElseThrow(() -> new RuntimeException(""));

        RentDTO resultRentDTO = rentService.rentInformation(reservationDTO,member.getId() );

        Assertions.assertThat(resultRentDTO).isNotNull();

        Optional<Rent> savedRent = rentRepository.findByRentCars_Name("소나타").stream().filter(s -> s.getMember().getEmail().equals("guildong@email.com")).findFirst();

        log.info("savedRent.getMember().getName():{}",savedRent.get().getMember().getName());

        Assertions.assertThat(savedRent.orElseThrow(()->new RuntimeException("회원을 찾을 수 없습니다")).getMember().getName()).isEqualTo("홍길동");
    }

}