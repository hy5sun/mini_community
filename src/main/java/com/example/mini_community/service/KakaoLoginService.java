package com.example.mini_community.service;

import com.example.mini_community.domain.KakaoProfile;
import com.example.mini_community.domain.KakaoToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Service
@Slf4j
public class KakaoLoginService {
    public KakaoToken getAccessToken(String clientId, String redirectUri, String code) throws IOException {
        String url = "https://kauth.kakao.com";

        WebClient client = WebClient.builder()
                .baseUrl(url)
                .defaultHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .build();

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();

        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", clientId);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("code", code);

        String result = client.post()
                .uri("/oauth/token")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoToken kakaoToken = null;

        try {
            kakaoToken = objectMapper.readValue(result, KakaoToken.class);
        } catch (JsonProcessingException e) {
            log.error("Exception Location : {}", e.getStackTrace()[0]);
            log.error("Error Message : {}", e.getMessage());
            log.error("Exception : {}", e.toString());
        }

        return kakaoToken;
    }

    public KakaoProfile getProfile(String accessToken) {
        String url = "https://kapi.kakao.com";

        WebClient wc = WebClient.builder()
                .baseUrl(url)
                .build();

        String result = wc.get()
                .uri("/v2/user/me")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;

        try {
            kakaoProfile = objectMapper.readValue(result, KakaoProfile.class);
        } catch (JsonProcessingException e) {
            log.error("Exception Location : {}", e.getStackTrace()[0]);
            log.error("Error Message : {}", e.getMessage());
            log.error("Exception : {}", e.toString());
        }

        return kakaoProfile;
    }
}
