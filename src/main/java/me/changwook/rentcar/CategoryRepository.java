package me.changwook.rentcar;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByRentCarsSegmentAndFuelType(RentCarsSegment rentCarsSegment, FuelType fuelType);

    Optional<Category> findByFuelTypeAndRentCarsSegment(FuelType fuelType, RentCarsSegment rentCarsSegment);
}
