package me.devhistory.member;

import me.devhistory.domain.Member;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findById(Long memberId);

    void validate(Long memberId);
}
