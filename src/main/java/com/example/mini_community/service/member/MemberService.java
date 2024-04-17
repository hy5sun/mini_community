package com.example.mini_community.service.member;

import com.example.mini_community.domain.member.Member;
import com.example.mini_community.dto.member.CreateMemberRequest;
import com.example.mini_community.repository.member.MemberRepository;
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

    @Transactional
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일에 해당하는 회원이 존재하지 않습니다."));
    }

    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id에 해당하는 회원이 존재하지 않습니다."));
    }
}
