package me.changwook.configuration.config;

import lombok.RequiredArgsConstructor;
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
@Profile({"test","local","default"})
@RequiredArgsConstructor
public class DataInitializer {

    private final DataInitializerService dataInitializerService;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            dataInitializerService.initializeData();
        };
    }

}