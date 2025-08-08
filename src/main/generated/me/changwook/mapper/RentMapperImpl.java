package me.changwook.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import me.changwook.DTO.RentDTO;
import me.changwook.domain.Rent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-08T15:17:14+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class RentMapperImpl implements RentMapper {

    @Autowired
    private RentCarsMapper rentCarsMapper;

    @Override
    public RentDTO rentToRentDTO(Rent rent) {
        if ( rent == null ) {
            return null;
        }

        RentDTO.RentDTOBuilder rentDTO = RentDTO.builder();

        rentDTO.rent_id( rent.getId() );
        rentDTO.rentTime( rent.getRentDate() );
        rentDTO.endTime( rent.getEndDate() );
        rentDTO.rentCars( rentCarsMapper.rentCarsToRentCarsDTO( rent.getRentCars() ) );
        rentDTO.duration( rent.getDuration() );

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

        rent.rentDate( rentDTO.getRentTime() );
        rent.endDate( rentDTO.getEndTime() );
        rent.rentCars( rentCarsMapper.rentCarsDTOToRent( rentDTO.getRentCars() ) );
        rent.duration( rentDTO.getDuration() );

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
}
