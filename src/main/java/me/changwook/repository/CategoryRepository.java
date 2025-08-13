package me.changwook.repository;

import me.changwook.domain.Category;
import me.changwook.domain.FuelType;
import me.changwook.domain.RentCarsSegment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    java.util.Optional<Category> findByRentCarsSegmentAndFuelType(RentCarsSegment rentCarsSegment, FuelType fuelType);
}
