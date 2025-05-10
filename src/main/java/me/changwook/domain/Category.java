package me.changwook.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void updateCategory(Category category) {
        this.rentCarsSegment = category.rentCarsSegment;
        this.fuelType = category.fuelType;
    }


}
