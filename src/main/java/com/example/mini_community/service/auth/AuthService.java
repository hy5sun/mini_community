package com.example.mini_community.service.auth;

import com.example.mini_community.common.config.jwt.TokenProvider;
import com.example.mini_community.common.exception.BusinessException;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.domain.refreshToken.RefreshToken;
import com.example.mini_community.dto.auth.*;
import com.example.mini_community.repository.member.MemberRepository;
import com.example.mini_community.repository.refreshToken.RefreshTokenRepository;
import com.example.mini_community.service.member.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.example.mini_community.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final TokenProvider tokenProvider;
    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public JoinResponse join(JoinRequest req) {
        Member member = Member.builder()
                .email(req.getEmail())
                .password(bCryptPasswordEncoder.encode(req.getPassword()))
                .nickname(req.getNickname())
                .profile_img(req.getProfile_img())
                .build();

        this.validateDuplicateMember(member);

        memberRepository.save(member);

        return JoinResponse.entityToDto(member);
    }

    @Transactional
    public void validateDuplicateMember(Member member) {
        if (memberRepository.existsByNickname(member.getNickname())) {
            throw new BusinessException(DUPLICATED_NICKNAME);
        } else if (memberRepository.existsMemberByEmail(member.getEmail())) {
            throw new BusinessException(DUPLICATED_EMAIL);
        }
    }

    @Transactional
    public LoginResponse signIn(LoginRequest loginRequest) {

        Member member = memberService.findByEmail(loginRequest.getEmail());

        if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new BusinessException(WRONG_PASSWORD);
        }

        if (refreshTokenRepository.findByMemberId(member.getId()) != null) {
            // 이미 있기 때문에 삭제하고 재발급
            refreshTokenRepository.deleteByMemberId(member.getId());
        }

        String accessToken = tokenProvider.generateToken(Duration.ofDays(14), member);
        String refreshToken = tokenProvider.generateToken(Duration.ofDays(31), member);

        // refreshToken 저장 (추후에 암호화 후 저장하도록 수정)
        RefreshToken rt = RefreshToken.builder()
                .member(member)
                .refreshToken(refreshToken)
                .build();

        refreshTokenRepository.save(rt);

        return LoginResponse.entityToDto(accessToken, refreshToken);
    }

    @Transactional
    public CreateAccessTokenResponse createNewAccessToken(Member member) {

        String refreshToken = member.getRefreshToken().getRefreshToken();

        if (!tokenProvider.validToken(refreshToken)) {
            throw new BusinessException(EXPIRED_TOKEN);
        }

        // 저장된 RT과 값이 같은지 확인
        if (!this.findRTByMemberId(member.getId()).getRefreshToken().equals(refreshToken)) {
            throw new BusinessException(INVALID_TOKEN);
        }

        String newAccessToken = tokenProvider.generateToken(Duration.ofDays(14), member);

        return CreateAccessTokenResponse.entityToDto(newAccessToken);
    }

    private RefreshToken findRTByMemberId(Long memberId) {
        return refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(TOKEN_NOT_FOUND));
    }

    public Member findMemberByToken(String token) {
        Long memberId = tokenProvider.getMemberId(token);
        return memberService.findById(memberId);
    }

    @Transactional
    public void deleteMember(Member member, PasswordValidateRequest req) {
        if(!bCryptPasswordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new BusinessException(WRONG_PASSWORD);
        }
        memberService.deleteMember(member);
    }
}
