package me.changwook.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import me.changwook.DTO.RentCarsDTO;
import me.changwook.DTO.RentDTO;
import me.changwook.domain.Rent;
import me.changwook.domain.RentCars;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-18T23:56:18+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.jar, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class RentMapperImpl implements RentMapper {

    @Override
    public RentDTO rentToRentDTO(Rent rent) {
        if ( rent == null ) {
            return null;
        }

        RentDTO.RentDTOBuilder rentDTO = RentDTO.builder();

        rentDTO.duration( rent.getDuration() );
        rentDTO.rentCars( rentCarsToRentCarsDTO( rent.getRentCars() ) );

        return rentDTO.build();
    }

    @Override
    public List<RentDTO> rentListToRentDTOs(List<Rent> rents) {
        if ( rents == null ) {
            return null;
        }

        List<RentDTO> list = new ArrayList<RentDTO>( rents.size() );
        for ( Rent rent : rents ) {
            list.add( rentToRentDTO( rent ) );
        }

        return list;
    }

    @Override
    public Rent rentDTOToRent(RentDTO rentDTO) {
        if ( rentDTO == null ) {
            return null;
        }

        Rent.RentBuilder rent = Rent.builder();

        rent.duration( rentDTO.getDuration() );
        rent.rentCars( rentCarsDTOToRentCars( rentDTO.getRentCars() ) );

        return rent.build();
    }

    @Override
    public List<Rent> rentDTOListToRentList(List<RentDTO> rentList) {
        if ( rentList == null ) {
            return null;
        }

        List<Rent> list = new ArrayList<Rent>( rentList.size() );
        for ( RentDTO rentDTO : rentList ) {
            list.add( rentDTOToRent( rentDTO ) );
        }

        return list;
    }

    protected RentCarsDTO rentCarsToRentCarsDTO(RentCars rentCars) {
        if ( rentCars == null ) {
            return null;
        }

        RentCarsDTO.RentCarsDTOBuilder rentCarsDTO = RentCarsDTO.builder();

        rentCarsDTO.name( rentCars.getName() );
        rentCarsDTO.rentPrice( rentCars.getRentPrice() );
        rentCarsDTO.recommend( rentCars.getRecommend() );
        rentCarsDTO.rentCarNumber( rentCars.getRentCarNumber() );
        rentCarsDTO.reservationStatus( rentCars.getReservationStatus() );

        return rentCarsDTO.build();
    }

    protected RentCars rentCarsDTOToRentCars(RentCarsDTO rentCarsDTO) {
        if ( rentCarsDTO == null ) {
            return null;
        }

        RentCars.RentCarsBuilder rentCars = RentCars.builder();

        rentCars.rentCarNumber( rentCarsDTO.getRentCarNumber() );
        rentCars.name( rentCarsDTO.getName() );
        rentCars.recommend( rentCarsDTO.getRecommend() );
        rentCars.rentPrice( rentCarsDTO.getRentPrice() );
        rentCars.reservationStatus( rentCarsDTO.getReservationStatus() );

        return rentCars.build();
    }
}
