package me.changwook.repository;

import me.changwook.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberSDJRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(String username);

}
