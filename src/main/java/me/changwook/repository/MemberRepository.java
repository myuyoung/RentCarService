package me.changwook.repository;

import me.changwook.domain.Member;
import me.changwook.repository.custom.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID>, MemberRepositoryCustom{

    Optional<Member> findByEmail(String email);

    //회원이 있는지 확인하는 쿼리
    Boolean existsByEmail(String email);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.rent where m.id= :id")
    Optional<Member> findByIdWithRents(@Param("id") UUID id);

}
