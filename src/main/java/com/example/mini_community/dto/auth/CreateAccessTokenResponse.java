package com.example.mini_community.dto.auth;

import com.example.mini_community.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateAccessTokenResponse {
    private String accessToken;

    public static CreateAccessTokenResponse entityToDto(String accessToken) {
        return new CreateAccessTokenResponse(accessToken);
    }
}
