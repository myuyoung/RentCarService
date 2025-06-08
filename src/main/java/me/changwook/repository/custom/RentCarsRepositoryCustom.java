package me.changwook.repository.custom;


import me.changwook.domain.RentCars;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface RentCarsRepositoryCustom {

    List<RentCars> findRecentlyRentedCars(LocalDateTime now);

    Page<RentCars> findAllByOrderByRecommendDesc(Pageable pageable);
}
