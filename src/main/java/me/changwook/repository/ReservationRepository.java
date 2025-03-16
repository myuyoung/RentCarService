package me.changwook.repository;

import me.changwook.domain.Rent;
import me.changwook.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findRentByRentId(Long rentId);

}
