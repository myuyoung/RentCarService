package me.changwook.repository;

import lombok.extern.slf4j.Slf4j;
import me.changwook.domain.*;
import me.changwook.mapper.RentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class RentRepositoryTest {

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private RentMapper rentMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RentCarsRepository rentCarsRepository;
    
    // 테스트 데이터 준비를 위한 메소드
    private Category createTestCategory() {
        return Category.builder()
                .fuelType(FuelType.GASOLINE)
                .rentCarsSegment(RentCarsSegment.MEDIUM)
                .build();
    }
    
    private RentCars createTestRentCars(Category category) {
        return RentCars.builder()
                .rentCarNumber("52바3205")
                .rentPrice(50000)
                .name("소나타")
                .recommend(50L)
                .totalDistance(35000)
                .category(category)
                .build();
    }
    
    private Rent createTestRent(RentCars rentCars) {
        return Rent.builder()
                .endDate(LocalDate.now().plusDays(2).atStartOfDay())
                .duration(2)
                .rentDate(LocalDate.now().atStartOfDay())
                .rentCars(rentCars)
                .build();
    }

    @Test
    @Transactional
    void save() {
        // Given
        Category testCategory = createTestCategory();
        categoryRepository.save(testCategory);
        
        RentCars testRentCars = createTestRentCars(testCategory);
        rentCarsRepository.save(testRentCars);
        
        Rent testRent = createTestRent(testRentCars);
        rentRepository.save(testRent);
        
        // When
        List<Rent> foundRents = rentRepository.findByRentCars_Name(testRentCars.getName());
        
        // Then
        log.info("TestRent.getRentCars().getRentCarNumber(): {}", testRent.getRentCars().getRentCarNumber());
        log.info("foundRents car numbers: {}", 
                foundRents.stream()
                        .map(s -> s.getRentCars().getRentCarNumber())
                        .toList());

        assertThat(foundRents)
                .isNotEmpty()
                .allSatisfy(rent -> assertThat(rent.getRentCars().getRentCarNumber())
                        .isEqualTo(testRentCars.getRentCarNumber()));

        assertThat(foundRents.getFirst().getRentCars().getRentCarNumber())
                .isEqualTo(testRentCars.getRentCarNumber());
    }
    
    @Test
    @Transactional
    void saveShouldPersistRentEntity() {
        // Given
        Category testCategory = createTestCategory();
        categoryRepository.save(testCategory);
        
        RentCars testRentCars = createTestRentCars(testCategory);
        rentCarsRepository.save(testRentCars);
        
        Rent testRent = createTestRent(testRentCars);
        
        // When
        Rent savedRent = rentRepository.save(testRent);
        
        // Then
        assertThat(savedRent.getId()).isNotNull();
        assertThat(savedRent.getRentCars().getRentCarNumber())
                .isEqualTo(testRentCars.getRentCarNumber());
    }
}