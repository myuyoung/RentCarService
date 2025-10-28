package me.changwook.reservation;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.changwook.member.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static me.changwook.member.QMember.member;
import static me.changwook.rentcar.QCategory.category;
import static me.changwook.rentcar.QRentCars.rentCars;
import static me.changwook.reservation.QReservation.reservation;


@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    //회원의 렌트값이 존재한다면 -> 기간을 검색하는 쿼리
    @Override
    public List<Reservation> findUserOverlappingReservations(Member memberParam, LocalDateTime startDate, LocalDateTime endDate){
        if(memberParam == null){
            return List.of();
        }
        return jpaQueryFactory.selectFrom(reservation)
                .where(
                        reservation.member.eq(memberParam),
                        reservation.rentDate.loe(endDate),
                        reservation.endDate.goe(startDate)
                )
                .fetch();
    }

    /**
     * 【기존 N+1 문제 해결】회원의 미래 예약 조회 - fetchJoin 적용
     */
    @Override
    public List<Reservation> findByDuration(UUID memberID) {
        return jpaQueryFactory.selectFrom(reservation)
                .leftJoin(reservation.member, member).fetchJoin()
                .leftJoin(reservation.rentCars, rentCars).fetchJoin()
                .leftJoin(reservation.rentCars.category, category).fetchJoin()
                .where(
                        reservation.member.id.eq(memberID),
                        reservation.rentDate.after(LocalDateTime.now())
                )
                .fetch();
    }

    /**
     * 【QueryDSL N+1 해결】차량명으로 예약 조회 - 모든 연관 엔티티 fetchJoin
     * 
     * 기존 문제점:
     * - findByRentCars_Name()은 1 + 3N개 쿼리 실행 (Reservation + Member*N + RentCars*N + Category*N)
     * - 특정 차량의 예약 목록 조회 시 심각한 성능 저하
     * 
     * 해결 방법:
     * - leftJoin().fetchJoin()으로 모든 연관 엔티티를 한 번에 조회
     * - 1개 쿼리로 모든 데이터 로딩 완료
     * 
     * @param rentCarsName 차량명
     * @return Member, RentCars, Category가 함께 로딩된 Reservation 목록
     */
    @Override
    public List<Reservation> findByRentCarsNameWithFetchJoin(String rentCarsName) {
        return jpaQueryFactory.selectFrom(reservation)
                .leftJoin(reservation.member, member).fetchJoin()           // Member N+1 해결
                .leftJoin(reservation.rentCars, rentCars).fetchJoin()       // RentCars N+1 해결
                .leftJoin(reservation.rentCars.category, category).fetchJoin() // Category N+1 해결
                .where(reservation.rentCars.name.eq(rentCarsName))
                .fetch();
    }
}
