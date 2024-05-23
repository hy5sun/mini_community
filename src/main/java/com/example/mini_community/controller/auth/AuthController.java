package com.example.mini_community.controller.auth;

import com.example.mini_community.domain.member.Member;
import com.example.mini_community.dto.auth.*;
import com.example.mini_community.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<JoinResponse> join(@Validated @RequestBody JoinRequest req) {
        Member savedMember = authService.join(req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JoinResponse.entityToDto(HttpStatus.CREATED.value(), "회원가입에 성공하셨습니다.", savedMember));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@Validated @RequestBody LoginRequest loginRequest) {
        Map<String, String> tokens = authService.signIn(loginRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new LoginResponse("로그인에 성공하셨습니다.", tokens.get("accessToken"), tokens.get("refreshToken")));
    }

    @PostMapping("/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken (@RequestBody CreateAccessTokenRequest req) {
        String newAccessToken = authService.createNewAccessToken(req.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse("accessToken을 정상적으로 발급했습니다.", newAccessToken));
    }
}
