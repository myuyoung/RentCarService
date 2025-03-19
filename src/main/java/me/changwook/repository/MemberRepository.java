package me.changwook.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import me.changwook.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findOne(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Optional<Member> findByName(String name) {
        return Optional.ofNullable(em.createQuery("select m from Member m where m.name = :name", Member.class
        ).setParameter("name", name).getSingleResult());
    }


}
