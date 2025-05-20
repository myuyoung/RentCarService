package me.changwook.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.changwook.DTO.RentCarsDTO;
import me.changwook.domain.RentCars;
import me.changwook.mapper.RentCarsMapper;
import me.changwook.repository.RentCarsRepository;
import me.changwook.service.BasicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


}
