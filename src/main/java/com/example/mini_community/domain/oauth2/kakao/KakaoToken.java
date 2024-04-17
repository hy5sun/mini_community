package com.example.mini_community.domain.oauth2.kakao;

import lombok.Data;

@Data
public class KakaoToken {
    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;
    private int refresh_token_expires_in;
    private String scope;
}
