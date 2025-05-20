package me.changwook.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentCars {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rentcars_id",updatable = false)
    private Long id;

    private String rentCarNumber;

    private String name;

    private Long recommend;

    private int rentPrice;

    private int totalDistance;

    


    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public void updateRentCars(RentCars rentCars) {

        if (rentCars.getRentCarNumber() != null) {
            this.rentCarNumber = rentCars.getRentCarNumber();
        }
        if (rentCars.getName() != null) {
            this.name = rentCars.getName();
        }
        if (rentCars.getRecommend() != null) {
            this.recommend = rentCars.getRecommend();
        }
        if (rentCars.getRentPrice() != 0) {
            this.rentPrice = rentCars.getRentPrice();
        }
        if (rentCars.getTotalDistance() != 0) {
            this.totalDistance = rentCars.getTotalDistance();
        }
        if (rentCars.getCategory() != null) {
            this.category = rentCars.getCategory();
        }
    }

}