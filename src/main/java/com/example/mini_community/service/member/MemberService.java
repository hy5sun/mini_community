package com.example.mini_community.service.member;

import com.example.mini_community.common.exception.BusinessException;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.mini_community.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }

    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }
}
