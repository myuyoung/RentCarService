package me.changwook.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.changwook.domain.ReservationStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentCarsDTO {

    @NotNull
    private String name;

    @NotNull
    @Min(0)
    private int rentPrice;

    private Long recommend;

    private String rentCarNumber;

    private ReservationStatus reservationStatus;

    @Min(0)
    private int totalDistance;
}
