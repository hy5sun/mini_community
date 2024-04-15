package com.example.mini_community.repository;

import com.example.mini_community.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByNickname(String nickname);
    Optional<Member> findByEmail(String email);
    Member findByNickname(String nickname);
}
