package me.changwook.repository.custom;


import me.changwook.domain.RentCars;

import java.time.LocalDateTime;
import java.util.List;

public interface RentCarsRepositoryCustom {

    List<RentCars> findRecentlyRentedCars(LocalDateTime now);
}
