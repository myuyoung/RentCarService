package me.changwook.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rent extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false,name = "rent_id", columnDefinition = "UUID")
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private LocalDateTime rentDate;

    private int duration;

    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
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
        if (rent.getRentCars() != null){
            this.rentCars = rent.getRentCars();
        }
        if ( rent.getMember() != null){
            this.member = rent.getMember();
        }
    }


}
