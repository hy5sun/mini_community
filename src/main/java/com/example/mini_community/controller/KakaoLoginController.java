package com.example.mini_community.controller;

import com.example.mini_community.domain.KakaoToken;
import com.example.mini_community.service.KakaoLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping()
@Slf4j
public class KakaoLoginController {

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @Autowired
    private KakaoLoginService kakaoLoginService;

    @GetMapping("/oauth/kakao")
    public String getAccessToken(@RequestParam("code") String code) throws IOException {
        KakaoToken kakaoToken = kakaoLoginService.getAccessToken(clientId, redirectUri, code);

        return kakaoToken.getAccess_token();
    }
}
