package me.changwook.mapper;

import me.changwook.DTO.RentDTO;
import me.changwook.domain.Rent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentMapper  {
    RentMapper INSTANCE = Mappers.getMapper(RentMapper.class);

    RentDTO rentToRentDTO(Rent rent);

    List<RentDTO> rentListToRentDTOs(List<Rent> rents);

    @Mapping(target = "id",ignore = true)
    Rent rentDTOToRent(RentDTO rentDTO);

    List<Rent> rentDTOListToRentList(List<RentDTO> rentList);



}
