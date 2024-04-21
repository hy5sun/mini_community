package com.example.mini_community.service.auth;

import com.example.mini_community.common.config.jwt.TokenProvider;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.domain.refreshToken.RefreshToken;
import com.example.mini_community.dto.auth.LoginRequest;
import com.example.mini_community.dto.auth.JoinRequest;
import com.example.mini_community.repository.member.MemberRepository;
import com.example.mini_community.repository.refreshToken.RefreshTokenRepository;
import com.example.mini_community.service.member.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenProvider tokenProvider;
    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Member join(JoinRequest req) {
        Member member = Member.builder()
                .email(req.getEmail())
                .password(bCryptPasswordEncoder.encode(req.getPassword()))
                .nickname(req.getNickname())
                .profile_img(req.getProfile_img())
                .build();

        this.validateDuplicateMember(member);

        return memberRepository.save(member);
    }

    @Transactional
    public void validateDuplicateMember(Member member) {
        if (memberRepository.existsByNickname(member.getNickname())) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }

    @Transactional
    public Map<String, String> signIn(LoginRequest loginRequest) {

        Member member = memberService.findByEmail(loginRequest.getEmail());

        if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (refreshTokenRepository.findByMemberId(member.getId()) != null) {
            // 이미 있기 때문에 삭제하고 재발급
            refreshTokenRepository.deleteByMemberId(member.getId());
        }

        String accessToken = tokenProvider.generateToken(Duration.ofHours(2), member);
        String refreshToken = tokenProvider.generateToken(Duration.ofDays(7), member);

        // refreshToken 저장 (추후에 암호화 후 저장하도록 수정)
        RefreshToken rt = RefreshToken.builder()
                .member(member)
                .refreshToken(refreshToken)
                .build();

        refreshTokenRepository.save(rt);

        // 클라이언트에게 전달
        HashMap<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    @Transactional
    public String  createNewAccessToken(String refreshToken) {
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        Long memberId = tokenProvider.getMemberId(refreshToken);

        // 저장된 RT과 값이 같은지 확인
        if (!this.findRTByMemberId(memberId).getRefreshToken().equals(refreshToken)) {
            throw new IllegalArgumentException("잘못된 값의 토큰입니다.");
        }

        Member member = memberService.findById(memberId);

        String newAccessToken = tokenProvider.generateToken(Duration.ofHours(2), member);

        return newAccessToken;
    }

    private RefreshToken findRTByMemberId(Long memberId) {
        return refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자의 token이 존재하지 않습니다."));
    }
}
