package com.example.mini_community.controller;

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

    @PostMapping("/signin")
    public ResponseEntity<Object> signIn (@RequestBody LoginRequest loginRequest) {
        Map<String, String> tokens = authService.signIn(loginRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new LoginResponse("로그인에 성공하셨습니다.", tokens.get("accessToken"), tokens.get("refreshToken")));
    }
}
