package me.changwook.repository;


import me.changwook.domain.RentCars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RentCarsRepository extends JpaRepository<RentCars, Long> {

    @Query("select r from RentCars r")
    List<RentCars> findAllByCarId();

    @Query("select c from Category c")
    Optional<List<RentCars>> findAllCategories();

    Optional<RentCars> findByName(String name);

    Optional<RentCars> findByRentCarNumber(String rentCarNumber);

    //사용 가능한 모든 차량 조회
    List<RentCars> findByAvailableTrue();

    //사용 가능한 차량을 이름으로 검색
    Optional<RentCars> findByNameAndAvailableTrue(String name);

}
