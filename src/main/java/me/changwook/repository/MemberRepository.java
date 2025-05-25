package me.changwook.repository;

import me.changwook.domain.Member;
import me.changwook.repository.custom.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

    Optional<Member> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.rent WHERE m.id = :id")
    Optional<Member> findByIdWithRents(@Param("id") Long id);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.rent")
    List<Member> findAllWithRents();
}
