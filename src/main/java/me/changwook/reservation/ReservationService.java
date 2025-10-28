package me.changwook.reservation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.mapper.ReservationMapper;
import me.changwook.reservation.dto.ReservationDTO;
import me.changwook.member.Member;
import me.changwook.rentcar.RentCars;
import me.changwook.exception.custom.MemberNotFoundException;
import me.changwook.exception.custom.ReservationConflictException;
import me.changwook.member.MemberRepository;
import me.changwook.rentcar.RentCarsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final RentCarsRepository rentCarsRepository;
    private final MemberRepository memberRepository;

    /**
     *
     * @param reservationDTO 예약정보를 받아오는 DTO
     * @param memberId 회원정보의 기본키를 받아오는 매개변수
     * @return 예약을 완료하고 차량의 정보와 예약 정보를 반환함
     */
    @Transactional
    public ReservationDTO reservation(ReservationDTO reservationDTO, UUID memberId) {
        //렌트카 정보 찾기
        RentCars rentCar = rentCarsRepository.findByRentCarNumber(reservationDTO.getRentCarsDTO().getRentCarNumber()).orElseThrow(()-> new EntityNotFoundException("차량 정보가 확인되지 않습니다."));
        log.info("렌트카 정보:{}",rentCar);

        //회원 찾기
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        log.info("회원 정보:{}",member);

        validateReservation(member, rentCar, reservationDTO);

        // ---
        //예약이 완료됨

        Reservation reservation = reservationMapper.reservationDTOToReservation(reservationDTO);
        log.info("예약 정보:{}",reservation);

        member.addMemberAndReservation(reservation);
        reservation.setRentCar(rentCar);

        Reservation viewReservationInfo = reservationRepository.save(reservation);

        //받은 DTO를 왜 그대로 반환하지 않는가? -> 검증하기 위함. 제대로 예약이 저장되고 그 데잍터가 정확하게 출력되는지 확인하기 위함.
        return reservationMapper.reservationToReservationDTO(viewReservationInfo);
    }

    //예약의 유효성을 검증하는 로직
    private void validateReservation(Member member, RentCars rentCar , ReservationDTO reservationDTO) {

        //ReservationDTO에서 예약 시작일과 종료일을 가져오기
        LocalDateTime newStartDate = reservationDTO.getRentTime();
        LocalDateTime newEndDate = reservationDTO.getEndTime();

        //예약에 대한 유효성 검사
        if (!newStartDate.isBefore(newEndDate)) {
            throw new IllegalArgumentException("예약 시작일은 종료일보다 이전이어야 합니다.");
        }

        //새로운 예약 기간과 겹치는 기존 예약이 있는지 확인하는 로직
        List<Reservation> overLappingReservations = reservationRepository.findOverLappingReservations(rentCar, newStartDate, newEndDate);
        if(!overLappingReservations.isEmpty()){
            throw new ReservationConflictException("해당 차량 (" + rentCar.getName() + ")은 요청하신 기간" +
                    newStartDate+ " ~ " + newEndDate + ")에 이미 예약되어 있습니다.");
        }

        //사용자의 면허 유효성 검사
        if(!member.getLicence()){
            throw new RuntimeException(member.getName() + "님은 운전면허가 확인되지 않았습니다.");
        }

        //사용자가 이미 예약한 상태라면 예약할 수 없음
        //Querydsl을 이용함
        List<Reservation> userOverlapping = reservationRepository.findUserOverlappingReservations(member,newStartDate,newEndDate);

        if(!userOverlapping.isEmpty()){
            throw new RuntimeException(member.getName() +"님은 요청하신 기간(" + newStartDate + " ~ " + newEndDate + ")에 예약이 존재합니다.");
        }
    }

    //시스템 시간 이후의 예약리스트를 보여주는 로직
    public List<ReservationDTO> findReservationList(UUID memberId) {
        List<Reservation> reservations = reservationRepository.findALLByMemberId(memberId);

        return reservationMapper.reservationListToReservationDTOs(reservations);
    }

    //선택한 예약을 보여주는 로직
    public ReservationDTO findReservation(UUID memberId) {
        Reservation reservation = reservationRepository.findByMemberId(memberId).orElseThrow(()->new EntityNotFoundException("예약이 존재하지 않습니다."));

        return reservationMapper.reservationToReservationDTO(reservation);
    }

    //예약을 취소하는 로직
    @Transactional
    public void cancelReservation(UUID memberId,UUID rentId) {
        Member member = memberRepository.findByIdWithRents(memberId).orElseThrow(() -> new EntityNotFoundException("회원과 관련된 예약이 존재하지 않습니다."));

        member.getReservation().stream().filter(r -> r.getId().equals(rentId)).findFirst().ifPresent(reservationRepository::delete);
    }

    // 관리자 전용 메서드들
    
    @Transactional(readOnly = true)
    public Page<ReservationDTO> getAllRentals(Pageable pageable) {
        Page<Reservation> rentals = reservationRepository.findAll(pageable);
        return rentals.map(reservationMapper::reservationToReservationDTO);
    }

    @Transactional(readOnly = true)
    public long getTotalRentalCount() {
        return reservationRepository.count();
    }

    @Transactional(readOnly = true)
    public long getActiveRentalCount() {
        LocalDateTime now = LocalDateTime.now();
        return reservationRepository.countByEndDateAfter(now);
    }
}
