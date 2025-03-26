package me.changwook.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.changwook.domain.RentCars;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentCarsDTO {

    @NotNull
    private String name;

    @NotNull
    private int rentPrice;

    private Long recommend;

}
