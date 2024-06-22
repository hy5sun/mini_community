package com.example.mini_community.dto.board;

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
public class CreateBoardRequest {
    
    @NotBlank(message = "제목을 입력하지 않았습니다.")
    @Size(min=2, max=16, message = "제목은 2글자 이상, 16글자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "내용을 입력하지 않았습니다.")
    private String content;
}
