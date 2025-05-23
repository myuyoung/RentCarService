package me.changwook.repository;

import me.changwook.domain.Rent;
import me.changwook.domain.RentCars;
import me.changwook.domain.ReservationStatus;
import me.changwook.repository.custom.RentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent,Long>, RentRepositoryCustom {

    List<Rent> findByDuration(int duration);

    List<Rent> findByRentDate(LocalDate rentDate);

    List<Rent> findByRentCars(RentCars rentCars);

    List<Rent> findByRentCars_Name(String rentCarsName);

    //새로운 예약 기간과 겹치는 기존 예약이 있는지 확인하는 쿼리
    @Query("""
    select r FROM Rent r WHERE r.rentCars = :rentCars\s
    AND r.rentDate <= :newEndDate \s
    AND r.endDate >= :newStartDate \s
       \s""")
    List<Rent> findOverLappingReservations(@Param("rentCars") RentCars rentCars,
                                           @Param("newStartDate") LocalDateTime newStartDate,
                                           @Param("newEndDate") LocalDateTime newEndDate);



}
