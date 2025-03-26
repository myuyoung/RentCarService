package me.changwook.mapper;

import me.changwook.DTO.RentCarsDTO;
import me.changwook.domain.RentCars;

public class RentCarsMapper {
    public static RentCars toEntity(RentCarsDTO dto) {
        return RentCars.builder().name(dto.getName()).rentPrice(dto.getRentPrice()).recommend(dto.getRecommend()).build();
    }

    public static RentCarsDTO toDTO(RentCars entity) {
        return new RentCarsDTO(entity.getName(), entity.getRentPrice(), entity.getRecommend());
    }

}
