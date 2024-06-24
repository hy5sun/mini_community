package com.example.mini_community.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class EditMemberRequest {
    @NotBlank(message="닉네임을 입력하지 않았습니다.")
    private String nickname;

    private String profile_img;
}
