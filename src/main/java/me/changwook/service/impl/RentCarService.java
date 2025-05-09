package me.changwook.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.changwook.DTO.RentCarsDTO;
import me.changwook.domain.RentCars;
import me.changwook.mapper.impl.RentCarsMapper;
import me.changwook.repository.RentCarsRepository;
import me.changwook.service.BasicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
public class RentCarService implements BasicService<RentCarsDTO> {

    private final RentCarsRepository rentCarsRepository;
    private final RentCarsMapper rentCarsMapper;


    @Override
    @Transactional
    public void save(RentCarsDTO rentCarsDTO) {
        rentCarsRepository.save(rentCarsMapper.rentCarsDTOToRent(rentCarsDTO));
    }

    @Override
    @Transactional
    public void update(RentCarsDTO rentCarsDTO) {
        RentCars rentCars = rentCarsRepository.findByName(rentCarsDTO.getName()).orElseThrow(() -> new RuntimeException("RentCars not found"));
       //업데이트 할 메서드를 작성해야함
        rentCars.updateRentCars(rentCarsMapper.rentCarsDTOToRent(rentCarsDTO));
    }

    @Override
    public void delete(RentCarsDTO rentCarsDTO) {
        rentCarsRepository.delete(rentCarsMapper.rentCarsDTOToRent(rentCarsDTO));
    }

    @Override
    public List<RentCarsDTO> findAll(RentCarsDTO rentCarsDTO) {
        return List.of(null);
    }


}
