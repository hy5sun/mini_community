package com.example.mini_community.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateMemberRequest {

    private String email;
    private String password;
    private String nickname;
    private String profile_img;
}
