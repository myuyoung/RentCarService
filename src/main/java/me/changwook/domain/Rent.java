package me.changwook.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,name = "rent_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private LocalDateTime rentDate;

    private int duration;

    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "rentcars_id")
    private RentCars rentCars;


    //연관관계 메서드(protected로 외부호출 불가 오로지 내부에서만 사용할 목적)
    protected void setMemberReference(Member member){
        this.member = member;
    }

    //연관관계 메서드-> RentCars
    public void setRentCar(RentCars rentCars){
        this.rentCars = rentCars;
    }

    public void updateRent(Rent rent){
        if(rent.getRentDate() != null) {
            this.rentCars = rent.getRentCars();
        }
        if(rent.getDuration() != 0) {
            this.duration = rent.getDuration();
        }
        if(rent.getEndDate() != null) {
            this.endDate = rent.getEndDate();
        }
        if (rent.getRentDate() != null){
            this.rentDate = rent.getRentDate();
        }
        if ( rent.getMember() != null){
            this.member = rent.getMember();
        }
    }


}
