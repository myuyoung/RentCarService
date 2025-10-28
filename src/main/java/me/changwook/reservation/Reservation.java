package me.changwook.reservation;

import jakarta.persistence.*;
import lombok.*;
import me.changwook.common.BaseEntity;
import me.changwook.member.Member;
import me.changwook.rentcar.RentCars;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, name = "rent_id", columnDefinition = "BINARY(16)")
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "BINARY(16)")
    private Member member;

    private LocalDateTime rentDate;

    private int duration;

    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rentcars_id")
    private RentCars rentCars;


    //연관관계 메서드
    public void setMemberReference(Member member){
        this.member = member;
    }

    //연관관계 메서드-> RentCars
    public void setRentCar(RentCars rentCars){
        this.rentCars = rentCars;
    }

    public void updateRent(Reservation reservation){
        if(reservation.getRentDate() != null) {
            this.rentCars = reservation.getRentCars();
        }
        if(reservation.getDuration() != 0) {
            this.duration = reservation.getDuration();
        }
        if(reservation.getEndDate() != null) {
            this.endDate = reservation.getEndDate();
        }
        if (reservation.getRentCars() != null){
            this.rentCars = reservation.getRentCars();
        }
        if ( reservation.getMember() != null){
            this.member = reservation.getMember();
        }
    }


}
