package me.changwook.repository.custom;

import me.changwook.domain.Member;
import me.changwook.domain.Rent;

import java.time.LocalDate;
import java.util.List;

public interface RentRepositoryCustom {

    List<Rent> findUserOverlappingReservations(Member member, LocalDate startDate, LocalDate endDate);
}
