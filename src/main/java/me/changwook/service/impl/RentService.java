package me.changwook.service.impl;

import lombok.RequiredArgsConstructor;
import me.changwook.domain.Rent;
import me.changwook.repository.RentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentService {

    private final RentRepository rentRepository;


}
