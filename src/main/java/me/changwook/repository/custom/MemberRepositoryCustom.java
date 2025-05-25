package me.changwook.repository.custom;

import me.changwook.domain.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<Member> findByIdWithRent(Long id);
}
