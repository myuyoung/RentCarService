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
public class RentCarService implements BasicService<RentCarsDTO> {

    private final RentCarsRepository rentCarsRepository;


    @Override
    @Transactional
    public void save(RentCarsDTO entity) {
        rentCarsRepository.save(RentCarsMapper.toEntity(entity));
    }

    @Override
    @Transactional
    public void update(RentCarsDTO entity) {
        RentCars rentCars = rentCarsRepository.findByName(entity.getName()).orElseThrow(() -> new RuntimeException("RentCars not found"));
        rentCars.updateRentCars(entity);
    }

    @Override
    public void delete(RentCarsDTO entity) {
        rentCarsRepository.delete(RentCarsMapper.toEntity(entity));
    }

    @Override
    public List<RentCarsDTO> findAll(RentCarsDTO entity) {
        return List.of(null);
    }


}
