package me.changwook.repository.custom;

import me.changwook.domain.Member;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepositoryCustom {

    Optional<Member> findByIdWithRent(UUID id);

}
