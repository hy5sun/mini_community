package com.example.mini_community.service;

import com.example.mini_community.domain.Member;
import com.example.mini_community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private final MemberRepository memberRepository;

    @Override
    public Member loadUserByUsername(String nickname) throws UsernameNotFoundException {
        return memberRepository.findByNickname(nickname);
    }
}
