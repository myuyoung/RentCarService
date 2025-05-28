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

    List<Rent> findByRentCars_Name(String rentCarsName);

    void deleteByRentCars(RentCars rentCars);

    //특정 기간의 모든 Rent 정보를 Member와 RentCars 정보와 함께 조회
    @Query("SELECT r FROM Rent r JOIN FETCH r.member JOIN FETCH r.rentCars WHERE r.rentDate = :rentDate")
    List<Rent> findAllWithDetailsByRentDate(LocalDate rentDate);

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
