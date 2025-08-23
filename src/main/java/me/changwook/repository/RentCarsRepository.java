package me.changwook.repository;


import jakarta.persistence.LockModeType;
import me.changwook.domain.RentCars;
import me.changwook.repository.custom.RentCarsRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RentCarsRepository extends JpaRepository<RentCars, Long>, RentCarsRepositoryCustom {

    @Query("select rc from RentCars rc")
    List<RentCars> findAllByCarId();

    @Query("select c from Category c")
    Optional<List<RentCars>> findAllCategories();

    Optional<RentCars> findByName(String name);

    Optional<RentCars> findByRentCarNumber(String rentCarNumber);

    @Query("SELECT rc FROM RentCars rc LEFT JOIN FETCH rc.category")
    List<RentCars> findAllWithCategory();

    // 커스텀 구현에서 제공하는 메서드 시그니처 노출(정렬 기반 추천)
    Page<RentCars> findAllByOrderByRecommendDesc(Pageable pageable);

    // 메인 추천용 정렬(가용성/최근대여/추천/연식/가격)
    Page<RentCars> findRecommendedForHome(Pageable pageable);
}