package me.changwook.rentcar;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface RentCarsRepositoryCustom {

    List<RentCars> findRecentlyRentedCars(LocalDateTime now);

    Page<RentCars> findAllByOrderByRecommendDesc(Pageable pageable);

    // 메인 추천용 정렬(가용성/최근대여/추천/연식/가격)
    Page<RentCars> findRecommendedForHome(Pageable pageable);

    Page<RentCars> searchCars(
            String segment,
            String fuelType,
            String keyword,
            Integer minPrice,
            Integer maxPrice,
            Pageable pageable
    );
}
