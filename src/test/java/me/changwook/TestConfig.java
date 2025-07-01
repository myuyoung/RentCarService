package me.changwook;

import me.changwook.mapper.*;
import me.changwook.service.NotificationService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public NotificationService notificationService() {
        return new MockTestEmailService();
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
