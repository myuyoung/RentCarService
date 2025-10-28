package me.changwook.reservation;

import me.changwook.member.Member;
import me.changwook.rentcar.RentCars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID>, ReservationRepositoryCustom {

    void deleteByRentCars(RentCars rentCars);

    //특정 기간의 모든 Reservation 정보를 Member와 RentCars 정보와 함께 조회
    @Query("SELECT r FROM Reservation r JOIN FETCH r.member JOIN FETCH r.rentCars WHERE r.rentDate = :rentDate")
    List<Reservation> findAllWithDetailsByRentDate(LocalDateTime rentDate);

    //새로운 예약 기간과 겹치는 기존 예약이 있는지 확인하는 쿼리
    @Query("""
    select r FROM Reservation r WHERE r.rentCars = :rentCars\s
    AND r.rentDate <= :newEndDate \s
    AND r.endDate >= :newStartDate \s
       \s""")
    List<Reservation> findOverLappingReservations(@Param("rentCars") RentCars rentCars,
                                                  @Param("newStartDate") LocalDateTime newStartDate,
                                                  @Param("newEndDate") LocalDateTime newEndDate);

    // 관리자 기능을 위한 메서드
    long countByEndDateAfter(LocalDateTime dateTime);

    Optional<Reservation> findByMemberId(UUID memberId);

    List<Reservation> findALLByMemberId(UUID memberId);

}
