package com.example.mini_community.controller.auth;

import com.example.mini_community.common.config.annotation.Login;
import com.example.mini_community.common.response.CustomResponse;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.dto.auth.*;
import com.example.mini_community.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public CustomResponse join(@Validated @RequestBody JoinRequest req) {
        JoinResponse savedMember = authService.join(req);

        return CustomResponse.response(HttpStatus.CREATED, "회원가입에 성공하셨습니다.", savedMember);
    }

    @PostMapping("/login")
    public CustomResponse login (@Validated @RequestBody LoginRequest loginRequest) {
        LoginResponse tokens = authService.signIn(loginRequest);

        return CustomResponse.response(HttpStatus.OK, "로그인에 성공하셨습니다.", tokens);
    }

    @PostMapping("/token")
    public CustomResponse createNewAccessToken (@Login Member member) {
        CreateAccessTokenResponse newAccessToken = authService.createNewAccessToken(member);

        return CustomResponse.response(HttpStatus.CREATED, "accessToken을 정상적으로 발급했습니다.", newAccessToken);
    }
}
