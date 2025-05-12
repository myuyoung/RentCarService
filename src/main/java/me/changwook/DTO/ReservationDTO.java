package me.changwook.DTO;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReservationDTO {

    @NotNull
    private RentDTO rentDTO;

    @NotNull
    private RentCarsDTO rentCarsDTO;
}
