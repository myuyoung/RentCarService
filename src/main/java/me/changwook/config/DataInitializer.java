package me.changwook.config;

import lombok.RequiredArgsConstructor;
import me.changwook.admin.CarRegistrationSubmission;
import me.changwook.admin.SubmissionStatus;
import me.changwook.member.Member;
import me.changwook.member.Role;
import me.changwook.rentcar.Category;
import me.changwook.rentcar.FuelType;
import me.changwook.rentcar.RentCars;
import me.changwook.rentcar.RentCarsSegment;
import me.changwook.admin.CarRegistrationSubmissionRepository;
import me.changwook.rentcar.CategoryRepository;
import me.changwook.member.MemberRepository;
import me.changwook.rentcar.RentCarsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import me.changwook.chat.ChatService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
@Profile({"test", "local", "local2","prod"})
@RequiredArgsConstructor
public class DataInitializer {

    private final CategoryRepository categoryRepository;
    private final RentCarsRepository rentCarsRepository;
    private final MemberRepository memberRepository;
    private final CarRegistrationSubmissionRepository submissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final ChatService chatService;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            initializeData();
        };
    }

    public void initializeData() {

        // 카테고리 초기화
        Category mediumGasoline = getOrCreateCategory(FuelType.GASOLINE, RentCarsSegment.MEDIUM);
        Category largeDiesel = getOrCreateCategory(FuelType.DIESEL, RentCarsSegment.LARGE);
        Category smallGasoline = getOrCreateCategory(FuelType.GASOLINE, RentCarsSegment.SMALL);
        Category mediumLPG = getOrCreateCategory(FuelType.LPG, RentCarsSegment.MEDIUM);
        Category largeGasoline = getOrCreateCategory(FuelType.GASOLINE, RentCarsSegment.LARGE);
        Category suvDiesel = getOrCreateCategory(FuelType.DIESEL, RentCarsSegment.SUV);
        Category suvGasoline = getOrCreateCategory(FuelType.GASOLINE, RentCarsSegment.SUV);
        Category mediumElectric = getOrCreateCategory(FuelType.ELECTRIC, RentCarsSegment.MEDIUM);

        List<RentCars> carList = Arrays.asList(
                RentCars.builder().rentCarNumber("11가1111").name("소나타").rentPrice(50000).category(mediumGasoline).build(),
                RentCars.builder().rentCarNumber("22나2222").name("벤츠 E클래스").rentPrice(100_000).category(largeDiesel).build(),
                RentCars.builder().rentCarNumber("33다3333").name("모닝").rentPrice(35000).category(smallGasoline).build(),
                RentCars.builder().rentCarNumber("44라4444").name("K5 (LPG)").rentPrice(48000).category(mediumLPG).build(),
                RentCars.builder().rentCarNumber("55마5555").name("그랜저").rentPrice(70000).category(largeGasoline).build(),
                RentCars.builder().rentCarNumber("66바6666").name("싼타페").rentPrice(80000).category(suvDiesel).build(),
                RentCars.builder().rentCarNumber("77사7777").name("팰리세이드").rentPrice(90000).category(suvDiesel).build(),
                RentCars.builder().rentCarNumber("88아8888").name("아이오닉 5").rentPrice(75000).category(mediumElectric).build(),
                RentCars.builder().rentCarNumber("99자9999").name("아반떼").rentPrice(45000).category(mediumGasoline).build(),
                RentCars.builder().rentCarNumber("10가0000").name("K8").rentPrice(72000).category(largeGasoline).build(),
                RentCars.builder().rentCarNumber("11나1234").name("스포티지 (Gasoline)").rentPrice(68000).category(suvGasoline).build(),
                RentCars.builder().rentCarNumber("12다5678").name("투싼 (Diesel)").rentPrice(70000).category(suvDiesel).build(),
                RentCars.builder().rentCarNumber("13라9012").name("레이").rentPrice(40000).category(smallGasoline).build(),
                RentCars.builder().rentCarNumber("14마3456").name("EV6").rentPrice(78000).category(mediumElectric).build(),
                RentCars.builder().rentCarNumber("15바7890").name("G80").rentPrice(120000).category(largeGasoline).build()
        );

        // --- 3. 렌트카 반복문으로 저장 (중복 방지) ---
        for (RentCars car : carList) {
            // 기존의 중복 방지 로직을 그대로 사용
            if (rentCarsRepository.findByRentCarNumber(car.getRentCarNumber()).isEmpty()) {
                rentCarsRepository.save(car);
            }
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

        // 채팅방 초기화
        initializeChatRooms();
    }

    /**
     * 기본 채팅방들을 생성합니다.
     */
    private void initializeChatRooms() {
        try {
            // Support 채팅방 생성
            createChatRoomIfNotExists("support", "고객지원", "문의사항이나 도움이 필요하시면 언제든지 이용해주세요.");
            
            // General 채팅방 생성
            createChatRoomIfNotExists("general", "자유 토론", "자유롭게 대화를 나누는 공간입니다.");
            
            // Notice 채팅방 생성
            createChatRoomIfNotExists("notice", "공지사항", "중요한 공지사항을 전달하는 채널입니다.");
            
        } catch (Exception e) {
            // 채팅방 초기화 실패는 전체 애플리케이션을 중단시키지 않음
            System.err.println("채팅방 초기화 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 채팅방이 존재하지 않는 경우에만 생성합니다.
     * 
     * @param roomId 채팅방 ID
     * @param roomName 채팅방 이름
     * @param description 채팅방 설명
     */
    private void createChatRoomIfNotExists(String roomId, String roomName, String description) {
        try {
            // 이미 존재하는 채팅방인지 확인
            if (chatService.getRoomById(roomId).isEmpty()) {
                chatService.createRoom(roomId, roomName, description);
                System.out.println("기본 채팅방 생성: " + roomId + " - " + roomName);
            } else {
                System.out.println("채팅방 이미 존재: " + roomId + " - " + roomName);
            }
        } catch (IllegalArgumentException e) {
            // 이미 존재하는 경우는 정상 상황
            System.out.println("채팅방 이미 존재: " + roomId + " - " + roomName);
        } catch (Exception e) {
            System.err.println("채팅방 " + roomId + " 생성 실패: " + e.getMessage());
        }
    }

    /**
     * 카테고리가 존재하면 찾아서 반환하고, 없으면 생성하여 저장한 뒤 반환합니다.
     * @param fuelType 연료 타입
     * @param segment 차량 세그먼트
     * @return 저장되거나 조회된 Category 객체
     */
    private Category getOrCreateCategory(FuelType fuelType, RentCarsSegment segment) {
        // CategoryRepository에 findByFuelTypeAndRentCarsSegment 메서드가 필요합니다.
        return categoryRepository.findByFuelTypeAndRentCarsSegment(fuelType, segment)
                .orElseGet(() -> {
                    Category newCategory = Category.builder()
                            .fuelType(fuelType)
                            .rentCarsSegment(segment)
                            .build();
                    return categoryRepository.save(newCategory);
                });
    }

}