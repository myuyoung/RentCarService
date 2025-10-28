package me.changwook.rentcar;

import jakarta.persistence.*;
import lombok.*;
import me.changwook.common.BaseEntity;
import me.changwook.reservation.ReservationStatus;

import java.time.LocalDate;

@Table(name = "rent_cars", indexes = {
        @Index(name = "idx_rent_car_number", columnList = "rentCarNumber", unique = true)
})
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentCars extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rentcars_id",updatable = false)
    private Long id;

    private String rentCarNumber;

    private String name;

    private Long recommend;

    private int rentPrice;

    private int totalDistance;

    // Todo  "소유자" 등급의 사용자는 반드시 하나 이상의 차량을 등록해야 함.

    //정기 검사 유효기간
    private LocalDate inspectionValidityPeriod;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReservationStatus reservationStatus = ReservationStatus.AVAILABLE;

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
        if (rentCars.getReservationStatus() != null) {
            this.reservationStatus = rentCars.getReservationStatus();
        }
    }

    public void updateReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

}