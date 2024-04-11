package com.example.mini_community.repository;

import com.example.mini_community.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByNickname(String nickname);

    Member findByNickname(String nickname);
}
