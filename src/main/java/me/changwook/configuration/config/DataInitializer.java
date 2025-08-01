package me.changwook.configuration.config;

import me.changwook.domain.*;
import me.changwook.repository.CategoryRepository;
import me.changwook.repository.MemberRepository;
import me.changwook.repository.RentCarsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@Profile("test")
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            CategoryRepository categoryRepository,
            RentCarsRepository rentCarsRepository,
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder){
        return args -> {
            // 카테고리 초기화
            Category mediumGasoline = Category.builder().fuelType(FuelType.GASOLINE).rentCarsSegment(RentCarsSegment.MEDIUM).build();
            Category largeDiesel = Category.builder().fuelType(FuelType.DIESEL).rentCarsSegment(RentCarsSegment.LARGE).build();
            categoryRepository.saveAll(Arrays.asList(mediumGasoline,largeDiesel));

            // 렌트카 초기화
            RentCars sonata = RentCars.builder().rentCarNumber("11가1111").name("소나타").rentPrice(50000).category(mediumGasoline).build();
            RentCars bentz = RentCars.builder().rentCarNumber("22나2222").name("벤츠 E클래스").rentPrice(100_000).category(largeDiesel).build();
            rentCarsRepository.saveAll(Arrays.asList(sonata,bentz));

            // 회원 초기화 - 이메일 중복 검사 추가
            String email1 = "guildong@email.com";
            String email2 = "general@email.com";
            String adminEmail = "admin@rentcar.com";
            
            // 관리자 계정 추가
            if (!memberRepository.existsByEmail(adminEmail)) {
                Member admin = Member.builder()
                        .name("시스템 관리자")
                        .email(adminEmail)
                        .password(passwordEncoder.encode("Admin123!"))
                        .licence(true)
                        .address("서울시 중구")
                        .phone("02-0000-0000")
                        .role(Role.ADMIN)
                        .build();
                memberRepository.save(admin);
            }
            
            // 첫 번째 사용자 추가 또는 건너뛰기
            if (!memberRepository.existsByEmail(email1)) {
                Member user1 = Member.builder()
                        .name("홍길동")
                        .email(email1)
                        .password(passwordEncoder.encode("Password123!"))
                        .licence(true)
                        .address("서울시 은평구")
                        .phone("010-1234-5678")
                        .role(Role.USER)
                        .build();
                memberRepository.save(user1);
            }
            
            // 두 번째 사용자 추가 또는 건너뛰기
            if (!memberRepository.existsByEmail(email2)) {
                Member user2 = Member.builder()
                        .name("이순신")
                        .email(email2)
                        .password(passwordEncoder.encode("Password1234@"))
                        .licence(false)
                        .address("경기도 안산")
                        .phone("02-1234-5678")
                        .role(Role.USER)
                        .build();
                memberRepository.save(user2);
            }
        };
    }
}