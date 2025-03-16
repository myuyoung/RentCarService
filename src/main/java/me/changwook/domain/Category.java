package me.changwook.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id",updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RentCarsSegment rentCarsSegment;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;


}
