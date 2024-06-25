package com.example.mini_community.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordValidateRequest {
    @NotBlank(message="비밀번호를 입력하지 않았습니다.")
    @Size(min=8, max=16, message = "비밀번호는 8글자 이상, 16글자 이하여야 합니다.")
    private String password;
}
