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
    date = "2025-05-19T23:57:20+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class RentMapperImpl implements RentMapper {

    @Override
    public RentDTO rentToRentDTO(Rent rent) {
        if ( rent == null ) {
            return null;
        }

        RentDTO.RentDTOBuilder rentDTO = RentDTO.builder();

        rentDTO.rentDate( rent.getRentDate() );
        rentDTO.duration( rent.getDuration() );
        rentDTO.endDate( rent.getEndDate() );
        rentDTO.rentCars( rentCarsToRentCarsDTO( rent.getRentCars() ) );

        return rentDTO.build();
    }

    @Override
    public List<RentDTO> rentDTOListToRentDTOs(List<Rent> rents) {
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

        rent.rentDate( rentDTO.getRentDate() );
        rent.duration( rentDTO.getDuration() );
        rent.endDate( rentDTO.getEndDate() );
        rent.rentCars( rentCarsDTOToRentCars( rentDTO.getRentCars() ) );

        return rent.build();
    }

    @Override
    public List<Rent> rentListToRents(List<RentDTO> rentDTOList) {
        if ( rentDTOList == null ) {
            return null;
        }

        List<Rent> list = new ArrayList<Rent>( rentDTOList.size() );
        for ( RentDTO rentDTO : rentDTOList ) {
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

        return rentCars.build();
    }
}
