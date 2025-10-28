package me.changwook.reservation;

import me.changwook.member.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepositoryCustom {

    List<Reservation> findUserOverlappingReservations(Member member, LocalDateTime startDate, LocalDateTime endDate);

    List<Reservation> findByDuration(UUID uuid);
    
    /**
     * 【QueryDSL N+1 해결】차량명으로 예약 조회 - 모든 연관 엔티티 fetchJoin
     * 기존 JPA 메서드 findByRentCars_Name()를 QueryDSL로 대체
     * 
     * @param rentCarsName 차량명
     * @return Member, RentCars, Category가 함께 로딩된 Reservation 목록
     */
    List<Reservation> findByRentCarsNameWithFetchJoin(String rentCarsName);
}
