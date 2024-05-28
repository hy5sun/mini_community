package com.example.mini_community.service.member;

import com.example.mini_community.common.exception.BusinessException;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static com.example.mini_community.common.exception.ErrorCode.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
