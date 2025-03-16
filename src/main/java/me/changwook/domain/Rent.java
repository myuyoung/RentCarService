package me.changwook.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    private LocalDate rentDate;

    private int duration;

    private LocalDate endDate;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "rentcars_id")
    private RentCars rentCars;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Reservation reservation;

    /*
    *  인스턴스 endDate 바꾸는 메서드
    * */
    public void rentDuration(LocalDate Ld) {
        this.endDate = Ld;
    }

    /*
    * endDate 리턴하는 메서드
    * */
    protected LocalDate getEndDate() {
        return endDate;
    }

    public void assignedMember(Member member) {
        this.member = member;
        member.setMember(this);
    }

    public void assignedReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
