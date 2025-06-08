package me.changwook.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentDTO {

    private UUID rent_id;

    @Future
    private LocalDateTime rentTime;

    @Min(1)
    private int duration;

    @Future
    private LocalDateTime endTime;

    @NotNull
    private RentCarsDTO rentCars;

}
