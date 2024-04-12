package com.example.mini_community.config.jwt;

import com.example.mini_community.domain.Member;
import com.example.mini_community.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해서 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        // given
        Member member = memberRepository.save(Member.builder()
                .email("user@gmail.com")
                .password("qwer1234")
                .nickname("mosunn")
                .profile_img("dd")
                .build());

        // when
        String token = tokenProvider.generateToken(Duration.ofDays(14), member);

        //then
        Long memberId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(memberId).isEqualTo(member.getId());
    }

    @DisplayName("validToken(): 만료된 토큰인 경우 유효성 검사 실패")
    @Test
    void validToken_invalidToken() {
        // given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build().createToken(jwtProperties);

        // when
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("validToken(): 유효한 토큰인 경우 유효성 검사 성공")
    @Test
    void validToken_validToken() {
        // given
        String token= JwtFactory.withDefaultValues().createToken(jwtProperties);

        // when
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        // given
        String memberEmail = "user@gmail.com";
        String token = JwtFactory.builder()
                .subject(memberEmail)
                .build().createToken(jwtProperties);

        // when
        Authentication authentication = tokenProvider.getAuthentication(token);

        // then
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(memberEmail);
    }

    @DisplayName("getMemberId(): 토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    void getMemberId() {
        // given
        Long memberId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", memberId))
                .build().createToken(jwtProperties);

        // when
        Long memberIdByToken = tokenProvider.getMemberId(token);

        // then
        assertThat(memberIdByToken).isEqualTo(memberId);
    }
}
