package me.changwook.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import me.changwook.DTO.RentCarsDTO;
import me.changwook.domain.RentCars;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-09T17:20:51+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class RentCarsMapperImpl implements RentCarsMapper {

    @Override
    public RentCarsDTO rentCarsToRentCarsDTO(RentCars rentCars) {
        if ( rentCars == null ) {
            return null;
        }

        RentCarsDTO.RentCarsDTOBuilder rentCarsDTO = RentCarsDTO.builder();

        rentCarsDTO.name( rentCars.getName() );
        rentCarsDTO.rentPrice( rentCars.getRentPrice() );
        rentCarsDTO.recommend( rentCars.getRecommend() );
        rentCarsDTO.rentCarNumber( rentCars.getRentCarNumber() );

        return rentCarsDTO.build();
    }

    @Override
    public List<RentCarsDTO> rentCarsDTOListToRentCarsDTOs(List<RentCars> rentCars) {
        if ( rentCars == null ) {
            return null;
        }

        List<RentCarsDTO> list = new ArrayList<RentCarsDTO>( rentCars.size() );
        for ( RentCars rentCars1 : rentCars ) {
            list.add( rentCarsToRentCarsDTO( rentCars1 ) );
        }

        return list;
    }

    @Override
    public RentCars rentCarsDTOToRent(RentCarsDTO rentCarsDTO) {
        if ( rentCarsDTO == null ) {
            return null;
        }

        RentCars.RentCarsBuilder rentCars = RentCars.builder();

        rentCars.rentCarNumber( rentCarsDTO.getRentCarNumber() );
        rentCars.name( rentCarsDTO.getName() );
        rentCars.recommend( rentCarsDTO.getRecommend() );
        rentCars.rentPrice( rentCarsDTO.getRentPrice() );

        return rentCars.build();
    }

    @Override
    public List<RentCars> rentListToRents(List<RentCarsDTO> rentCarsDTOList) {
        if ( rentCarsDTOList == null ) {
            return null;
        }

        List<RentCars> list = new ArrayList<RentCars>( rentCarsDTOList.size() );
        for ( RentCarsDTO rentCarsDTO : rentCarsDTOList ) {
            list.add( rentCarsDTOToRent( rentCarsDTO ) );
        }

        return list;
    }
}
