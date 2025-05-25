package me.changwook.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.changwook.domain.RentCars;
import me.changwook.repository.custom.RentCarsRepositoryCustom;

import java.time.LocalDateTime;
import java.util.List;

import static me.changwook.domain.QRent.rent;

@RequiredArgsConstructor
public class RentCarsRepositoryCustomImpl implements RentCarsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RentCars> findRecentlyRentedCars(LocalDateTime now) {
        return jpaQueryFactory.selectFrom(rent.rentCars).where(rent.endDate.after(LocalDateTime.now())
        ).fetch();
    }
}
