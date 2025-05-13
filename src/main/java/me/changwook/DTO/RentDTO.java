package me.changwook.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentDTO {

    @FutureOrPresent
    private LocalDate rentDate;

    @Min(value = 1)
    private int duration;

    @Future
    private LocalDate endDate;

    @NotNull
    private RentCarsDTO rentCars;
}
