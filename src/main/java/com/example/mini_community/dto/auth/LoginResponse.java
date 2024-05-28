package com.example.mini_community.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    private String accessToken;
    private String refreshToken;

    public static LoginResponse entityToDto(String accessToken, String refreshToken) {
        return new LoginResponse(accessToken, refreshToken);
    }
}
