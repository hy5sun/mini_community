package com.example.mini_community.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    private String message;
    private String accessToken;
    private String refreshToken;
}
