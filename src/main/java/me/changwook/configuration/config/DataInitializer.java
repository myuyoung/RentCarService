package me.changwook.configuration.config;

import me.changwook.domain.*;
import me.changwook.repository.CategoryRepository;
import me.changwook.repository.CarRegistrationSubmissionRepository;
import me.changwook.repository.MemberRepository;
import me.changwook.repository.RentCarsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

@Configuration
@Profile({"test","local"})
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            CategoryRepository categoryRepository,
            RentCarsRepository rentCarsRepository,
            MemberRepository memberRepository,
            CarRegistrationSubmissionRepository submissionRepository,
            PasswordEncoder passwordEncoder){
        return args -> {
            // 카테고리 초기화
            Category mediumGasoline = Category.builder().fuelType(FuelType.GASOLINE).rentCarsSegment(RentCarsSegment.MEDIUM).build();
            Category largeDiesel = Category.builder().fuelType(FuelType.DIESEL).rentCarsSegment(RentCarsSegment.LARGE).build();
            categoryRepository.saveAll(Arrays.asList(mediumGasoline,largeDiesel));

            // 렌트카 초기화: 중복 방지 (이미 존재하면 생성하지 않음)
            if (rentCarsRepository.findByRentCarNumber("11가1111").isEmpty()) {
                RentCars sonata = RentCars.builder()
                        .rentCarNumber("11가1111").name("소나타").rentPrice(50000).category(mediumGasoline).build();
                rentCarsRepository.save(sonata);
            }
            if (rentCarsRepository.findByRentCarNumber("22나2222").isEmpty()) {
                RentCars bentz = RentCars.builder()
                        .rentCarNumber("22나2222").name("벤츠 E클래스").rentPrice(100_000).category(largeDiesel).build();
                rentCarsRepository.save(bentz);
            }

            // 회원 초기화 - 이메일 중복 검사 추가 (실제 저장할 이메일과 동일한 값으로 체크)
            String user1Email = "email1@naver.com";
            String user2Email = "email2@naver.com";
            String adminEmail = "adminEmail@naver.com";
            
            // 관리자 계정 생성 또는 이메일 업데이트
            Optional<Member> existingAdmin = memberRepository.findAll().stream()
                    .filter(m -> m.getRole() == Role.ADMIN)
                    .findFirst();
            if (existingAdmin.isPresent()) {
                Member admin = existingAdmin.get();
                if (!adminEmail.equals(admin.getEmail())) {
                    // 타 계정이 이미 새 이메일을 사용 중이면 업데이트하지 않음
                    if (!memberRepository.existsByEmail(adminEmail)) {
                        Member update = Member.builder().email(adminEmail).build();
                        admin.updateMember(update);
                        memberRepository.save(admin);
                    }
                }
            } else {
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
            if (!memberRepository.existsByEmail(user1Email)) {
                Member user1 = Member.builder()
                        .name("홍길동")
                        .email(user1Email)
                        .password(passwordEncoder.encode("Password123!"))
                        .licence(true)
                        .address("서울시 은평구")
                        .phone("010-1234-5678")
                        .role(Role.USER)
                        .build();
                memberRepository.save(user1);
            }
            
            // 두 번째 사용자 추가 또는 건너뛰기
            if (!memberRepository.existsByEmail(user2Email)) {
                Member user2 = Member.builder()
                        .name("이순신")
                        .email(user2Email)
                        .password(passwordEncoder.encode("Password1234@"))
                        .licence(false)
                        .address("경기도 안산")
                        .phone("02-1234-5678")
                        .role(Role.USER)
                        .build();
                memberRepository.save(user2);
            }
            
            // 테스트용 차량 등록 신청 데이터 추가
            if (submissionRepository.count() == 0) {
                // 일반 사용자가 신청한 것으로 가정 
                Optional<Member> user1 = memberRepository.findByEmail(user1Email);
                if (user1.isPresent()) {
                    CarRegistrationSubmission testSubmission1 = CarRegistrationSubmission.builder()
                            .memberId(user1.get().getId())
                            .carName("아반떼")
                            .rentCarNumber("12가3456")
                            .rentPrice(60000)
                            .status(SubmissionStatus.PENDING)
                            .build();
                    submissionRepository.save(testSubmission1);

                    CarRegistrationSubmission testSubmission2 = CarRegistrationSubmission.builder()
                            .memberId(user1.get().getId())
                            .carName("BMW 3시리즈")
                            .rentCarNumber("34나7890")
                            .rentPrice(150000)
                            .status(SubmissionStatus.PENDING)
                            .build();
                    submissionRepository.save(testSubmission2);
                }
            }
        };
    }
}