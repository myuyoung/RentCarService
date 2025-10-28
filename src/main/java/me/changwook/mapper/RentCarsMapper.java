package me.changwook.mapper;

import me.changwook.rentcar.dto.RentCarsDTO;
import me.changwook.rentcar.RentCars;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentCarsMapper {
    RentCarsMapper INSTANCE = Mappers.getMapper(RentCarsMapper.class);

    RentCarsDTO rentCarsToRentCarsDTO(RentCars rentCars);

    List<RentCarsDTO> rentCarsListToRentCarsDTOs(List<RentCars> rentCars);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "inspectionValidityPeriod", ignore = true)
    @Mapping(target = "category", ignore = true)
    RentCars rentCarsDTOToRent(RentCarsDTO rentCarsDTO);

    List<RentCars> rentCarsDTOListToRentCars(List<RentCarsDTO> rentCarsDTOList);


}
