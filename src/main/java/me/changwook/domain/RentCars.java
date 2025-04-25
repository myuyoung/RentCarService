package me.changwook.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.changwook.DTO.RentCarsDTO;

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

    private String name;

    private Long recommend;

    private int rentPrice;

    private int totalDistance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public void updateRentCars(RentCarsDTO entity) {
        this.name = entity.getName();
        this.recommend = entity.getRecommend();
        this.rentPrice = entity.getRentPrice();
    }


}
