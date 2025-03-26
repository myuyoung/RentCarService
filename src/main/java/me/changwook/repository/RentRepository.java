package me.changwook.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import me.changwook.domain.Rent;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RentRepository {

    private final EntityManager em;


    public void save(Rent rent) {
        em.persist(rent);
    }

    public Rent findOne(Long id) {
         return em.find(Rent.class,id);
    }

    public List<Rent> findAll() {
        return em.createQuery("select r from Rent r", Rent.class).getResultList();
    }

    public Rent findByName(String name) {
        return em.find(Rent.class,name);
    }

}
