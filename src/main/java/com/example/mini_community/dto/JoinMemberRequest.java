package com.example.mini_community.dto;

import com.example.mini_community.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JoinMemberRequest {

    private String email;
    private String password;
    private String nickname;
    private String profile_img;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .profile_img(profile_img)
                .build();
    }
}
