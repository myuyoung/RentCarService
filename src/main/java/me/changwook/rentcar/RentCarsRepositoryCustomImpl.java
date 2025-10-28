package me.changwook.rentcar;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.dsl.CaseBuilder;
import lombok.RequiredArgsConstructor;
import me.changwook.reservation.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static me.changwook.rentcar.QCategory.category;
import static me.changwook.rentcar.QRentCars.rentCars;
import static me.changwook.reservation.QReservation.reservation;


@RequiredArgsConstructor
public class RentCarsRepositoryCustomImpl implements RentCarsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RentCars> findRecentlyRentedCars(LocalDateTime now) {
        return jpaQueryFactory.selectFrom(reservation.rentCars).where(reservation.endDate.after(LocalDateTime.now())
        ).fetch();
    }

    @Override
    public Page<RentCars> findAllByOrderByRecommendDesc(Pageable pageable) {
        List<RentCars> content = jpaQueryFactory
        .selectFrom(rentCars)
        .leftJoin(rentCars.category, category).fetchJoin()
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

    @Override
    public Page<RentCars> findRecommendedForHome(Pageable pageable) {
        var availabilityScore = new CaseBuilder()
                .when(rentCars.reservationStatus.eq(ReservationStatus.AVAILABLE)).then(1)
                .otherwise(0);

        LocalDateTime since = LocalDateTime.now().minusDays(30);

        List<RentCars> content = jpaQueryFactory
                .selectFrom(rentCars)
                .leftJoin(rentCars.category, category).fetchJoin()
                .leftJoin(reservation).on(reservation.rentCars.eq(rentCars)
                        .and(reservation.rentDate.after(since)))
                .groupBy(rentCars.id, category.id)
                .orderBy(
                        availabilityScore.desc(),
                        reservation.id.count().desc(),
                        rentCars.recommend.desc(),
                        category.modelYear.desc(),
                        rentCars.rentPrice.asc()
                )
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

    @Override
    public Page<RentCars> searchCars(
            String segment,
            String fuelType,
            String keyword,
            Integer minPrice,
            Integer maxPrice,
            Pageable pageable
    ) {
        var query = jpaQueryFactory.selectFrom(rentCars)
                .leftJoin(rentCars.category, category).fetchJoin();

        if (StringUtils.hasText(segment)) {
            query.where(category.rentCarsSegment.stringValue().equalsIgnoreCase(segment));
        }
        if (StringUtils.hasText(fuelType)) {
            query.where(category.fuelType.stringValue().equalsIgnoreCase(fuelType));
        }
        if (StringUtils.hasText(keyword)) {
            query.where(rentCars.name.containsIgnoreCase(keyword)
                    .or(rentCars.rentCarNumber.containsIgnoreCase(keyword)));
        }
        if (minPrice != null) {
            query.where(rentCars.rentPrice.goe(minPrice));
        }
        if (maxPrice != null) {
            query.where(rentCars.rentPrice.loe(maxPrice));
        }

        var availabilityScore = new CaseBuilder()
                .when(rentCars.reservationStatus.eq(ReservationStatus.AVAILABLE)).then(1)
                .otherwise(0);

        LocalDateTime since = LocalDateTime.now().minusDays(30);

        List<RentCars> content = query
                .leftJoin(reservation).on(reservation.rentCars.eq(rentCars)
                        .and(reservation.rentDate.after(since)))
                .groupBy(rentCars.id, category.id)
                .orderBy(
                        availabilityScore.desc(),
                        reservation.id.count().desc(),
                        rentCars.recommend.desc(),
                        category.modelYear.desc(),
                        rentCars.rentPrice.asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(rentCars.count())
                .from(rentCars)
                .leftJoin(rentCars.category, category)
                .fetchOne();

        long totalCount = total == null ? 0L : total;

        return new PageImpl<>(content, pageable, totalCount);
    }
}
