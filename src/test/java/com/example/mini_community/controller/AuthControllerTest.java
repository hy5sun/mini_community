package com.example.mini_community.controller;

import com.example.mini_community.common.config.jwt.JwtProperties;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.domain.refreshToken.RefreshToken;
import com.example.mini_community.dto.auth.JoinRequest;
import com.example.mini_community.dto.auth.LoginRequest;
import com.example.mini_community.repository.member.MemberRepository;
import com.example.mini_community.repository.refreshToken.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void setMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

//    @AfterEach
//    public void AfterEach() {
//        Member testMember = memberRepository.findByNickname("test");
//        if (testMember != null) {
//            if (testMember.getRefreshToken() != null) {
//                refreshTokenRepository.delete(testMember.getRefreshToken());
//            }
//            memberRepository.delete(testMember);
//        }
//    }

    @Order(1)
    @DisplayName("회원가입")
    @Test
    void join() throws Exception {

        final String url = "/auth/join";
        JoinRequest req = new JoinRequest("test@gmail.com", "qwer1234", "test", "ss.jpeg");
        final String requestBody = objectMapper.writeValueAsString(req);

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isCreated());

        List<Member> members = memberRepository.findAll();

        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getNickname()).isEqualTo("test");
        assertThat(members.get(0).getProfile_img()).isEqualTo("ss.jpeg");
    }

    @Order(2)
    @DisplayName("로그인")
    @Test
    void login() throws Exception {
        // given
        final String url = "/auth/login";

        LoginRequest req = new LoginRequest("test@gmail.com", "qwer1234");
        final String requestBody = objectMapper.writeValueAsString(req);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isOk());
    }

    @Order(3)
    @DisplayName("새로운 엑세스 토큰을 발급한다.")
    @Test
    public void createNewAccessToken() throws Exception {
        // given
        final String url = "/auth/token";

        Member testMember = memberRepository.findByNickname("test");

        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(testMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("memberId의 토큰이 존재하지 않습니다."));


        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.ALL)
                .content(String.valueOf(testMember)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }
}
