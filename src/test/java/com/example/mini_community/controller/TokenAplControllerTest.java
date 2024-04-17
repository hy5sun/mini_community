package com.example.mini_community.controller;

import com.example.mini_community.config.jwt.JwtFactory;
import com.example.mini_community.config.jwt.JwtProperties;
import com.example.mini_community.domain.Member;
import com.example.mini_community.domain.RefreshToken;
import com.example.mini_community.dto.auth.CreateAccessTokenRequest;
import com.example.mini_community.repository.MemberRepository;
import com.example.mini_community.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenAplControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void setMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @AfterEach
    public void afterEach() {
        Member testMember = memberRepository.findByNickname("test");
        refreshTokenRepository.delete(testMember.getRefreshToken());
        memberRepository.delete(testMember);
    }

    @DisplayName("createNewAccessToken: 새로운 엑세스 토큰을 발급한다.")
    @Test
    public void createNewAccessToken() throws Exception {
        // given
        final String url = "/auth/token";

        Member testMember = memberRepository.save(Member.builder()
                .email("test@gmail.com")
                .password("qwer1234")
                .nickname("test")
                .profile_img("ss.jpeg")
                .build());

        String refreshToken = JwtFactory.builder()
                .claims(Map.of("id", testMember.getId()))
                .build()
                .createToken(jwtProperties);

        refreshTokenRepository.save(
                RefreshToken.builder()
                .member(testMember)
                .refreshToken(refreshToken)
                .build()
        );

        CreateAccessTokenRequest req = new CreateAccessTokenRequest();
        req.setRefreshToken(refreshToken);
        final String reqBody = objectMapper.writeValueAsString(req);

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(reqBody));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }
}
