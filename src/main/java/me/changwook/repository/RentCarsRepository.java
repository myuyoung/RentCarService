package me.changwook.repository;


import me.changwook.domain.RentCars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RentCarsRepository extends JpaRepository<RentCars, Long> {

    @Query("select r from RentCars r")
    Optional<List<RentCars>> findAllByCarId();

    @Query("select c from Category c")
    Optional<List<RentCars>> findAllCategories();

    Optional<RentCars> findByName(String name);
}
