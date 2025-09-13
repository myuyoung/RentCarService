package me.changwook.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import me.changwook.DTO.RentCarsDTO;
import me.changwook.domain.RentCars;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-12T17:02:13+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.jar, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class RentCarsMapperImpl implements RentCarsMapper {

    @Override
    public RentCarsDTO rentCarsToRentCarsDTO(RentCars rentCars) {
        if ( rentCars == null ) {
            return null;
        }

        RentCarsDTO.RentCarsDTOBuilder rentCarsDTO = RentCarsDTO.builder();

        rentCarsDTO.id( rentCars.getId() );
        rentCarsDTO.name( rentCars.getName() );
        rentCarsDTO.rentPrice( rentCars.getRentPrice() );
        rentCarsDTO.recommend( rentCars.getRecommend() );
        rentCarsDTO.rentCarNumber( rentCars.getRentCarNumber() );
        rentCarsDTO.reservationStatus( rentCars.getReservationStatus() );
        rentCarsDTO.totalDistance( rentCars.getTotalDistance() );

        return rentCarsDTO.build();
    }

    @Override
    public List<RentCarsDTO> rentCarsListToRentCarsDTOs(List<RentCars> rentCars) {
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
        rentCars.totalDistance( rentCarsDTO.getTotalDistance() );
        rentCars.reservationStatus( rentCarsDTO.getReservationStatus() );

        return rentCars.build();
    }

    @Override
    public List<RentCars> rentCarsDTOListToRentCars(List<RentCarsDTO> rentCarsDTOList) {
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
