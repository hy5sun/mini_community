package com.example.mini_community.service;

import com.example.mini_community.domain.Member;
import com.example.mini_community.dto.CreateMemberRequest;
import com.example.mini_community.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Member join(CreateMemberRequest req) {
        Member member = Member.builder()
                .email(req.getEmail())
                .password(bCryptPasswordEncoder.encode(req.getPassword()))
                .nickname(req.getNickname())
                .profile_img(req.getProfile_img())
                .build();

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