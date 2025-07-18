package me.changwook.service;

import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.RentCarsDTO;
import me.changwook.TestConfig;
import me.changwook.domain.RentCars;
import me.changwook.repository.RentCarsRepository;
import me.changwook.service.impl.RentCarService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Import(TestConfig.class)
class RentCarServiceTest {

    @Autowired
    private RentCarService rentCarService;

    @Autowired
    private RentCarsRepository rentCarsRepository;

    @BeforeEach
    void setUp() {
        RentCars rentCars = RentCars.builder().name("Genesis").rentPrice(5000).recommend(42L).build();
        rentCarsRepository.save(rentCars);
    }

    @Test
    @Transactional
    void updateRent() {
        RentCarsDTO rentCarsDTO = RentCarsDTO.builder().rentPrice(1000).recommend(50L).name("Genesis").build();
        rentCarService.update(rentCarsDTO);

        RentCars name = rentCarsRepository.findByName("Genesis").orElseThrow(()->new RuntimeException("RentCars not found"));

        log.info("name.getRecommend(): {}", name.getRecommend());
        log.info("rentCarsDTO.getRecommend(): {}", rentCarsDTO.getRecommend());

        Assertions.assertThat(name.getRecommend()).isNotEqualTo(42L);
    }
}