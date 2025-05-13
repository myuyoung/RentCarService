package me.changwook.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.changwook.DTO.RentDTO;
import me.changwook.DTO.ReservationDTO;
import me.changwook.domain.Member;
import me.changwook.domain.Rent;
import me.changwook.domain.RentCars;
import me.changwook.mapper.impl.MemberMapper;
import me.changwook.mapper.impl.RentCarsMapper;
import me.changwook.mapper.impl.RentMapper;
import me.changwook.repository.MemberRepository;
import me.changwook.repository.RentCarsRepository;
import me.changwook.repository.RentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentService {

    private final RentRepository rentRepository;
    private final RentMapper rentMapper;
    private final RentCarsMapper rentCarsMapper;
    private final RentCarsRepository rentCarsRepository;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    //예약하는 정보를 받아와서 저장한 후 반환
    @Transactional
    protected Rent reservation(RentDTO rentDTO) {
        return rentRepository.save(rentMapper.rentDTOToRent(rentDTO));
    }

    //렌트카정보를 받아오고,회원의 정보를 받아오고,렌트를 하는 로직
    @Transactional
    public RentDTO rentInformation(ReservationDTO reservationDTO, String memberEmail) {
        //렌트카 정보 찾기
        RentCars rentCar = rentCarsRepository.findByRentCarNumber(reservationDTO.getRentCarsDTO().getRentCarNumber()).orElseThrow(()-> new EntityNotFoundException("차량 정보가 확인되지 않습니다."));

        //회원 찾기
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(()->new EntityNotFoundException("회원이 존재하지 않습니다."));

        //차량 대여 가능 여부 확인
        if(!rentCar.isAvailable()){
            throw new RuntimeException("이미 차량이 예약이 되어있습니다.");
        }

        //사용자의 면허 유효성 검사
        if(!member.getLicence()){
            throw new RuntimeException(member.getName() + "님은 운전면허가 확인되지 않았습니다");
        }

        //렌트를 저장하고 RentDTO를 반환
        Rent newRent = reservation(reservationDTO.getRentDTO());

        //엔티티 간 연관관계 설정 -> 더티체킹
        member.setMemberAndRent(newRent);
        newRent.setRentCar(rentCar);

        return rentMapper.rentToRentDTO(newRent);
    }


}
