package me.changwook.repository;


import me.changwook.domain.Rent;
import me.changwook.domain.RentCars;
import me.changwook.domain.ReservationStatus;
import me.changwook.repository.custom.RentCarsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentCarsRepository extends JpaRepository<RentCars, Long>, RentCarsRepositoryCustom {

    @Query("select r from RentCars r")
    List<RentCars> findAllByCarId();

    @Query("select c from Category c")
    Optional<List<RentCars>> findAllCategories();

    Optional<RentCars> findByName(String name);

    Optional<RentCars> findByRentCarNumber(String rentCarNumber);

    @Query("SELECT rc FROM RentCars rc LEFT JOIN FETCH rc.category")
    List<RentCars> findAllWithCategory();

}