package me.changwook.mapper;

import me.changwook.DTO.RentCarsDTO;
import me.changwook.domain.RentCars;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentCarsMapper {
    RentCarsMapper INSTANCE = Mappers.getMapper(RentCarsMapper.class);

    RentCarsDTO rentCarsToRentCarsDTO(RentCars rentCars);

    List<RentCarsDTO> rentCarsListToRentCarsDTOs(List<RentCars> rentCars);

    RentCars rentCarsDTOToRent(RentCarsDTO rentCarsDTO);

    List<RentCars> rentCarsDTOListToRentCars(List<RentCarsDTO> rentCarsDTOList);


}
