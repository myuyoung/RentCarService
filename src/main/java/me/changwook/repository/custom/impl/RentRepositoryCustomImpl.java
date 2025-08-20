package me.changwook.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.changwook.domain.Member;
import me.changwook.domain.Rent;
import me.changwook.repository.custom.RentRepositoryCustom;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static me.changwook.domain.QRent.rent;
import static me.changwook.domain.QMember.member;
import static me.changwook.domain.QRentCars.rentCars;
import static me.changwook.domain.QCategory.category;

@RequiredArgsConstructor
public class RentRepositoryCustomImpl implements RentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    //회원의 렌트값이 존재한다면 -> 기간을 검색하는 쿼리
    @Override
    public List<Rent> findUserOverlappingReservations(Member memberParam, LocalDateTime startDate, LocalDateTime endDate){
        if(memberParam == null){
            return List.of();
        }
        return jpaQueryFactory.selectFrom(rent)
                .where(
                        rent.member.eq(memberParam),
                        rent.rentDate.loe(endDate),
                        rent.endDate.goe(startDate)
                )
                .fetch();
    }

    /**
     * 【기존 N+1 문제 해결】회원의 미래 예약 조회 - fetchJoin 적용
     */
    @Override
    public List<Rent> findByDuration(UUID memberID) {
        return jpaQueryFactory.selectFrom(rent)
                .leftJoin(rent.member, member).fetchJoin()
                .leftJoin(rent.rentCars, rentCars).fetchJoin()
                .leftJoin(rent.rentCars.category, category).fetchJoin()
                .where(
                        rent.member.id.eq(memberID),
                        rent.rentDate.after(LocalDateTime.now())
                )
                .fetch();
    }

    /**
     * 【QueryDSL N+1 해결】차량명으로 예약 조회 - 모든 연관 엔티티 fetchJoin
     * 
     * 기존 문제점:
     * - findByRentCars_Name()은 1 + 3N개 쿼리 실행 (Rent + Member*N + RentCars*N + Category*N)
     * - 특정 차량의 예약 목록 조회 시 심각한 성능 저하
     * 
     * 해결 방법:
     * - leftJoin().fetchJoin()으로 모든 연관 엔티티를 한 번에 조회
     * - 1개 쿼리로 모든 데이터 로딩 완료
     * 
     * @param rentCarsName 차량명
     * @return Member, RentCars, Category가 함께 로딩된 Rent 목록
     */
    @Override
    public List<Rent> findByRentCarsNameWithFetchJoin(String rentCarsName) {
        return jpaQueryFactory.selectFrom(rent)
                .leftJoin(rent.member, member).fetchJoin()           // Member N+1 해결
                .leftJoin(rent.rentCars, rentCars).fetchJoin()       // RentCars N+1 해결
                .leftJoin(rent.rentCars.category, category).fetchJoin() // Category N+1 해결
                .where(rent.rentCars.name.eq(rentCarsName))
                .fetch();
    }
}
