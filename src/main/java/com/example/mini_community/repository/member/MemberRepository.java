package com.example.mini_community.repository.member;

import com.example.mini_community.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByNickname(String nickname);
    boolean existsMemberByEmail(String email);
    Optional<Member> findByEmail(String email);
    Member findByNickname(String nickname);
    Optional<Member> findById(Long id);
}
