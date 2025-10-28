package me.changwook.mapper;

import me.changwook.reservation.Reservation;
import me.changwook.reservation.dto.ReservationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RentCarsMapper.class})
public interface ReservationMapper {
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(target = "rent_id", source = "id")
    @Mapping(target = "rentTime", source = "rentDate")
    @Mapping(target = "endTime", source = "endDate")
    @Mapping(target = "rentCarsDTO", source = "rentCars")
    ReservationDTO reservationToReservationDTO(Reservation reservation);

    List<ReservationDTO> reservationListToReservationDTOs(List<Reservation> reservations);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "rentDate", source = "rentTime")
    @Mapping(target = "endDate", source = "endTime")
    @Mapping(target = "rentCars", source = "rentCarsDTO")
    Reservation reservationDTOToReservation(ReservationDTO reservationDTO);

    List<Reservation> reservationDTOListToReservationList(List<ReservationDTO> rentList);



}
