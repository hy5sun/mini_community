package com.example.mini_community.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberRequest {
    @NotBlank(message="이메일을 입력하지 않았습니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message="비밀번호를 입력하지 않았습니다.")
    @Size(min=8, max=16, message = "비밀번호는 8글자 이상, 16글자 이하여야 합니다.")
    private String password;

    @NotBlank(message="닉네임을 입력하지 않았습니다.")
    private String nickname;

    private String profile_img;
}
