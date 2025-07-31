package me.changwook.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.changwook.DTO.RentCarsDTO;
import me.changwook.domain.RentCars;
import me.changwook.mapper.RentCarsMapper;
import me.changwook.repository.RentCarsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Getter
public class RentCarService  {

    private final RentCarsRepository rentCarsRepository;
    private final RentCarsMapper rentCarsMapper;

    @Transactional
    public void save(RentCarsDTO rentCarsDTO) {
        rentCarsRepository.save(rentCarsMapper.rentCarsDTOToRent(rentCarsDTO));
    }


    @Transactional
    public void update(RentCarsDTO rentCarsDTO) {
        RentCars rentCars = rentCarsRepository.findByName(rentCarsDTO.getName()).orElseThrow(() -> new RuntimeException("RentCars not found"));

        rentCars.updateRentCars(rentCarsMapper.rentCarsDTOToRent(rentCarsDTO));
    }

    public void delete(RentCarsDTO rentCarsDTO) {
        rentCarsRepository.delete(rentCarsMapper.rentCarsDTOToRent(rentCarsDTO));
    }

    @Transactional(readOnly = true)
    public Page<RentCarsDTO> getRankedRentCars(Pageable pageable) {
        Page<RentCars> rentCarsPage = rentCarsRepository.findAllByOrderByRecommendDesc(pageable);
        return rentCarsPage.map(rentCarsMapper::rentCarsToRentCarsDTO);
    }

    @Transactional
    public void registerCar(RentCarsDTO rentCarsDTO) {
        String rentCarNumber = rentCarsDTO.getRentCarNumber();
        if(rentCarsRepository.findByRentCarNumber(rentCarNumber).isPresent()) {
            throw new RuntimeException("이미 등록되어 있는 차량입니다.");
        }
        //국토교통부 API로 부터 데이터를 받아서 역직렬화 시킨 후 매핑하고 DB에 저장하는 로직을 짜고싶은데 8월까지 API추가 허가권이 없다고 한다.
        else{
            rentCarsRepository.save(rentCarsMapper.rentCarsDTOToRent(rentCarsDTO));
        }
    }

    // 관리자 전용 메서드들
    
    @Transactional(readOnly = true)
    public Page<RentCarsDTO> getAllCars(Pageable pageable) {
        Page<RentCars> cars = rentCarsRepository.findAll(pageable);
        return cars.map(rentCarsMapper::rentCarsToRentCarsDTO);
    }

    @Transactional
    public void deleteRentCar(Long carId) {
        RentCars rentCar = rentCarsRepository.findById(carId)
            .orElseThrow(() -> new RuntimeException("차량을 찾을 수 없습니다."));
        rentCarsRepository.delete(rentCar);
    }

    @Transactional(readOnly = true)
    public long getTotalCarCount() {
        return rentCarsRepository.count();
    }
}
