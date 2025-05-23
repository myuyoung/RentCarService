package me.changwook.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.changwook.DTO.RentDTO;
import me.changwook.DTO.ReservationDTO;
import me.changwook.domain.Member;
import me.changwook.domain.Rent;
import me.changwook.domain.RentCars;
import me.changwook.exception.custom.ReservationConflictException;
import me.changwook.mapper.RentMapper;
import me.changwook.repository.MemberRepository;
import me.changwook.repository.RentCarsRepository;
import me.changwook.repository.RentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentService {

    private final RentRepository rentRepository;
    private final RentMapper rentMapper;
    private final RentCarsRepository rentCarsRepository;
    private final MemberRepository memberRepository;

    //예약하는 정보를 받아와서 저장한 후 반환
    @Transactional
    protected Rent reservation(RentDTO rentDTO,RentCars rentCar,Member member) {
        Rent rent = rentMapper.rentDTOToRent(rentDTO);
        //엔티티 간 연관관계 설정 -> 더티체킹
        member.setMemberAndRent(rent);
        rent.setRentCar(rentCar);
        return rentRepository.save(rent);
    }

    //렌트카정보를 받아오고,회원의 정보를 받아오고,렌트를 하는 로직
    @Transactional
    public RentDTO rentInformation(ReservationDTO reservationDTO, String memberEmail) {
        //렌트카 정보 찾기
        RentCars rentCar = rentCarsRepository.findByRentCarNumber(reservationDTO.getRentCarsDTO().getRentCarNumber()).orElseThrow(()-> new EntityNotFoundException("차량 정보가 확인되지 않습니다."));

        //회원 찾기
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(()->new EntityNotFoundException("회원이 존재하지 않습니다."));

        validateReservation(member, rentCar,reservationDTO);

        //예약이 완료됨
        Rent newRent = reservation(reservationDTO.getRentDTO(),rentCar,member);

        return rentMapper.rentToRentDTO(newRent);
    }

    private void validateReservation(Member member, RentCars rentCar ,ReservationDTO reservationDTO) {

        //ReservationDTO에서 예약 시작일과 종료일을 가져오기
        LocalDateTime newStartDate = reservationDTO.getRentDTO().getRentTime();
        LocalDateTime newEndDate = reservationDTO.getRentDTO().getEndTime();

        //예약에 대한 유효성 검사
        if (!newStartDate.isAfter(newEndDate)) {
            throw new IllegalArgumentException("예약 시작일은 종료일보다 이전이어야 합니다.");
        }

        //새로운 예약 기간과 겹치는 기존 예약이 있는지 확인하는 로직
        List<Rent> overLappingReservations = rentRepository.findOverLappingReservations(rentCar, newStartDate, newEndDate);
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
        List<Rent> userOverlapping = rentRepository.findUserOverlappingReservations(member,newStartDate,newEndDate);

        if(!userOverlapping.isEmpty()){
            throw new RuntimeException(member.getName() +"님은 요청하신 기간(" + newStartDate + " ~ " + newEndDate + ")에 예약이 존재합니다.");
        }
    }
}
