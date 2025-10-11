package me.changwook;

import me.changwook.mapper.*;
import me.changwook.service.NotificationService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;

@TestConfiguration
public class TestConfig  {

    @Bean
    @Primary
    public NotificationService notificationService() {
        return new MockTestEmailService();
    }

    @Bean
    @Primary
    //모든 테스트에서 사용할 가짜 JavaMailSender Bean등록
    public JavaMailSender javaMailSender() {
        return Mockito.mock(JavaMailSender.class);
    }

    @Bean
    public MemberMapper memberMapper() {
        return new MemberMapperImpl();
    }

    @Bean
    public RegisterMapper registerMapper() {
        return new RegisterMapperImpl();
    }

    @Bean
    public RentMapper rentMapper() {
        return new RentMapperImpl();
    }

    @Bean
    public RentCarsMapper rentCarsMapper() {
        return new RentCarsMapperImpl();
    }
}
