package me.changwook.reservation.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.changwook.rentcar.dto.RentCarsDTO;

//예약정보와 차량정보를 담는 DTO
@Data
@RequiredArgsConstructor
public class ReservationDTOWithRentCarDTO {

    @NotNull
    private ReservationDTO reservationDTO;

    @NotNull
    private RentCarsDTO rentCarsDTO;
}
