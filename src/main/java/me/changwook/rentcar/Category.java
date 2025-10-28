package me.changwook.rentcar;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id",updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RentCarsSegment rentCarsSegment;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    //출력
    private int power;

    //배기량
    private int engineDisplacement;

    //승차정원
    private int passengerCapacity;

    private LocalDate modelYear;

    public void updateCategory(Category category) {
        this.rentCarsSegment = category.rentCarsSegment;
        this.fuelType = category.fuelType;
    }


}
