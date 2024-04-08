package com.example.mini_community.service;

import com.example.mini_community.domain.Member;
import com.example.mini_community.dto.JoinMemberRequest;
import com.example.mini_community.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public Member join(JoinMemberRequest req) {
        Member member = req.toEntity();
        validateDuplicateMember(member);

        return memberRepository.save(member);
    }

    @Transactional
    public void validateDuplicateMember(Member member) {

        if (memberRepository.existsByNickname(member.getNickname())) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }
}
