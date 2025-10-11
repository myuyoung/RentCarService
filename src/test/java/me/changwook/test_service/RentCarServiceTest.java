package me.changwook.test_service;

import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.RentCarsDTO;
import me.changwook.domain.Category;
import me.changwook.domain.FuelType;
import me.changwook.domain.RentCars;
import me.changwook.domain.RentCarsSegment;
import me.changwook.repository.CategoryRepository;
import me.changwook.repository.RentCarsRepository;
import me.changwook.service.impl.RentCarService;
import me.changwook.util.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
class RentCarServiceTest extends IntegrationTest {

    @Autowired
    private RentCarService rentCarService;

    @Autowired
    private RentCarsRepository rentCarsRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        // Category 먼저 생성
        Category category = Category.builder()
                .fuelType(FuelType.GASOLINE)
                .rentCarsSegment(RentCarsSegment.LARGE)
                .power(300)
                .engineDisplacement(3000)
                .passengerCapacity(5)
                .modelYear(LocalDate.of(2023, 1, 1))
                .build();
        Category savedCategory = categoryRepository.save(category);
        
        // RentCars에 Category 연결
        RentCars rentCars = RentCars.builder()
                .name("Genesis")
                .rentPrice(5000)
                .recommend(42L)
                .rentCarNumber("01가1234")
                .category(savedCategory)
                .build();
        rentCarsRepository.save(rentCars);
    }

    @Test
    @Transactional
    void updateRent() {
        // 먼저 저장된 차량의 ID를 가져옴
        RentCars existingCar = rentCarsRepository.findByName("Genesis")
                .orElseThrow(() -> new RuntimeException("RentCars not found"));
        
        RentCarsDTO rentCarsDTO = RentCarsDTO.builder()
                .rentPrice(1000)
                .recommend(50L)
                .name("Genesis")
                .build();
        
        // updateCar 메서드 사용 (carId 필요)
        rentCarService.updateCar(existingCar.getId(), rentCarsDTO);

        RentCars updatedCar = rentCarsRepository.findByName("Genesis")
                .orElseThrow(() -> new RuntimeException("RentCars not found"));

        log.info("updatedCar.getRecommend(): {}", updatedCar.getRecommend());
        log.info("rentCarsDTO.getRecommend(): {}", rentCarsDTO.getRecommend());

        Assertions.assertThat(updatedCar.getRecommend()).isNotEqualTo(42L);
        Assertions.assertThat(updatedCar.getRecommend()).isEqualTo(50L);
    }
}