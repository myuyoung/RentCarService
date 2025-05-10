package me.changwook.DTO;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReservationDTO {

    private RentDTO rentDTO;

    private RentCarsDTO rentCarsDTO;
}
