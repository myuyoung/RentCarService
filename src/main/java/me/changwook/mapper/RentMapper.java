package me.changwook.mapper;

import me.changwook.DTO.RentDTO;
import me.changwook.domain.Rent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RentCarsMapper.class})
public interface RentMapper  {
    RentMapper INSTANCE = Mappers.getMapper(RentMapper.class);

    @Mapping(target = "rent_id", source = "id")
    @Mapping(target = "rentTime", source = "rentDate")
    @Mapping(target = "endTime", source = "endDate")
    @Mapping(target = "rentCars", source = "rentCars")
    RentDTO rentToRentDTO(Rent rent);

    List<RentDTO> rentListToRentDTOs(List<Rent> rents);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "rentDate", source = "rentTime")
    @Mapping(target = "endDate", source = "endTime")
    @Mapping(target = "rentCars", source = "rentCars")
    Rent rentDTOToRent(RentDTO rentDTO);

    List<Rent> rentDTOListToRentList(List<RentDTO> rentList);



}
