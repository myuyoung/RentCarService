package me.changwook.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.admin.CarRegistrationSubmissionRepository;
import me.changwook.chat.ChatService;
import me.changwook.member.*;
import me.changwook.member.dto.RegisterMemberDTO;
import me.changwook.rentcar.CategoryRepository;
import me.changwook.rentcar.RentCarsRepository;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Profile({"test", "local", "local2"})
@RequiredArgsConstructor
@Component
@Slf4j
public class DataInitializerImpl implements DataInitializer {

    private final CategoryRepository categoryRepository;
    private final RentCarsRepository rentCarsRepository;
    private final MemberRepository memberRepository;
    private final CarRegistrationSubmissionRepository submissionRepository;
    private final ChatService chatService;
    private final PasswordEncoder passwordEncoder;
    private final RegisterService registerService;


    @Override
    public List<String[]> dummyData() {
        List<String[]> credentialsList = new ArrayList<>();
        Faker faker = new Faker(Locale.KOREA);
        Faker faker2 = new Faker(Locale.US);
        for(int i = 0; i<=99; i++) {
            String uniqueEmail = faker2.name().firstName()+i+"@test.com";

            String password = "Test123!";

            RegisterMemberDTO registerMemberDTO = RegisterMemberDTO.builder()
                    .name(faker.name().fullName())
                    .email(uniqueEmail)
                    .phone(faker.phoneNumber().phoneNumber())
                    .password(password)
                    .address(faker.address().fullAddress())
                    .build();
            registerService.registerMember(registerMemberDTO);

            credentialsList.add(new String[]{uniqueEmail,password});
        }
        Member admin = Member.builder()
                .name("시스템 관리자")
                .email("adminEmail@naver.com")
                .password(passwordEncoder.encode("Admin123!"))
                .licence(true)
                .address("서울시 중구")
                .phone("02-0000-0000")
                .role(Role.ADMIN)
                .build();
        memberRepository.save(admin);

        return credentialsList;
    }

    @Override
    public void run(String... args) throws Exception {
        try{
            List<String[]> credentials = dummyData();

            writeCredentialsToCsv(credentials);

        }catch(Exception e){
            log.error("더미 데이터 입력 실패{}",e.getMessage());
        }
    }

    /**
     * k6가 읽을 CSV 파일을 생성하는 헬퍼 메서드
     *
     * @param credentialsList [email, rawPassword] 배열 리스트
     * @throws IOException
     */
    private void writeCredentialsToCsv(List<String[]> credentialsList) throws IOException {
        // 프로젝트 루트 경로에 'dummy-users.csv' 파일 생성
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("dummy-users.csv"))) {
            // CSV 헤더 작성
            writer.write("email,password");
            writer.newLine();

            // 데이터 작성
            for (String[] credential : credentialsList) {
                writer.write(credential[0] + "," + credential[1]);
                writer.newLine();
            }
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
}