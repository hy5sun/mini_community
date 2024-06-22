package com.example.mini_community.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateCommentRequest {
    @NotBlank(message = "내용을 입력하지 않았습니다.")
    private String content;
}
