package me.changwook.repository;

import me.changwook.domain.Rent;
import me.changwook.domain.RentCars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent,Long> {

    List<Rent> findByDuration(int duration);

    List<Rent> findByRentDate(LocalDate rentDate);

    List<Rent> findByRentCars(RentCars rentCars);

    List<Rent> findByRentCars_Name(String rentCarsName);


}
