package me.changwook.service;

import lombok.RequiredArgsConstructor;
import me.changwook.domain.Rent;
import me.changwook.repository.RentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentService {

    private final RentRepository rentRepository;

    public Rent validateRent(Rent rent) {
        Rent rent1 = rentRepository.findOne(rent.getMember().getId());
        if(!rent1.getMember().getLicence())  {
            throw new IllegalStateException("Rent is not licensed");
        }
        return rent1;
    }

}
