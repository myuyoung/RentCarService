package me.changwook.repository.custom;

import me.changwook.domain.Member;
import me.changwook.domain.Rent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RentRepositoryCustom {

    List<Rent> findUserOverlappingReservations(Member member, LocalDateTime startDate, LocalDateTime endDate);

    List<Rent> findByDuration(UUID uuid);
}
