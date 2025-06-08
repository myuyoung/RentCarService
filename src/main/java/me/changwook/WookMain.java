package me.changwook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class WookMain {

    public static void main(String[] args) {
        SpringApplication.run(WookMain.class, args);
    }
}
