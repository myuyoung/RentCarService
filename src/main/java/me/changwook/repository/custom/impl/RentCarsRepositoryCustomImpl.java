package me.changwook.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.changwook.domain.RentCars;
import me.changwook.repository.custom.RentCarsRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static me.changwook.domain.QRent.rent;
import static me.changwook.domain.QRentCars.rentCars;

@RequiredArgsConstructor
public class RentCarsRepositoryCustomImpl implements RentCarsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RentCars> findRecentlyRentedCars(LocalDateTime now) {
        return jpaQueryFactory.selectFrom(rent.rentCars).where(rent.endDate.after(LocalDateTime.now())
        ).fetch();
    }

    @Override
    public Page<RentCars> findAllByOrderByRecommendDesc(Pageable pageable) {
        List<RentCars> content = jpaQueryFactory
        .selectFrom(rentCars)
        .orderBy(rentCars.recommend.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

        Long total = jpaQueryFactory
                .select(rentCars.count())
                .from(rentCars)
                .fetchOne();

        long totalCount = total == null ? 0L : total;

        return new PageImpl<>(content, pageable, totalCount);
    }
}
