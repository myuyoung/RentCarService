package me.changwook.member;

import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import me.changwook.reservation.QReservation;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static me.changwook.member.QMember.member;
import static me.changwook.reservation.QReservation.reservation;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPQLQueryFactory jpaQueryFactory;


    /**
     * 회원이 현재시간 이후의 모든 예약을 모두 가져오는 로직
     * @param memberId 외부에서 받아오는 memeber의 primary key 값
     * @return Optional<Member>
     */
    @Override
    public Optional<Member> findByIdWithRent(UUID memberId) {
        Member foundMember = jpaQueryFactory
                .selectFrom(member)
                .leftJoin(member.reservation, reservation)
                .on(reservation.rentDate.after(LocalDateTime.now()))
                .fetchJoin()
                .where(
                        member.id.eq(memberId)
                ).fetchOne();
        return Optional.ofNullable(foundMember);
    }
}
