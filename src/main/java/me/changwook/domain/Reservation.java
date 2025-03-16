package me.changwook.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Reservation {

    @Id
    @GeneratedValue
    @Column(name = "reservation_id",updatable = false)
    private Long id ;

    @OneToMany(mappedBy = "reservation",cascade = CascadeType.ALL)
    private List<RentCars> rentCars  = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_id")
    private Rent rent;

    public void setReservation(Rent rent) {
        this.rent = rent;
        rent.assignedReservation(this);
    }
}
