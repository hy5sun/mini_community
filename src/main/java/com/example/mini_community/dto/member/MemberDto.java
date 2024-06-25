package com.example.mini_community.dto.member;

import com.example.mini_community.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberDto {
    private String email;
    private String profileImg;
    private String nickname;

    public static MemberDto toDto(Member member) {
        return new MemberDto(member.getEmail(), member.getProfile_img(), member.getNickname());
    }
}
