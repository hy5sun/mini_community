package com.example.mini_community.dto.auth;

import com.example.mini_community.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinResponse {
    private String email;
    private String nickname;

    public static JoinResponse entityToDto(Member member) {
        return new JoinResponse(member.getEmail(), member.getNickname());
    }
}
