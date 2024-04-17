package com.example.mini_community.controller.auth;

import com.example.mini_community.dto.auth.CreateAccessTokenRequest;
import com.example.mini_community.dto.auth.CreateAccessTokenResponse;
import com.example.mini_community.dto.auth.LoginRequest;
import com.example.mini_community.dto.auth.LoginResponse;
import com.example.mini_community.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@RequestBody LoginRequest loginRequest) {
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
