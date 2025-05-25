package me.changwook.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.changwook.domain.Member;
import me.changwook.domain.Rent;
import me.changwook.repository.custom.RentRepositoryCustom;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static me.changwook.domain.QMember.member;
import static me.changwook.domain.QRent.rent;

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


}
