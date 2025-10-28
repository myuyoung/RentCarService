package me.changwook.member;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepositoryCustom {

    Optional<Member> findByIdWithRent(UUID id);

}
