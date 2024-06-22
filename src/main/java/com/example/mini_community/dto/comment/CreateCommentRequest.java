package com.example.mini_community.dto.comment;

import com.example.mini_community.domain.comment.Comment;
import com.example.mini_community.domain.board.Board;
import com.example.mini_community.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateCommentRequest {
    @NotBlank(message = "내용을 입력하지 않았습니다.")
    private String content;

    public Comment toEntity(Member member, Board board) {
        return Comment.builder()
                .content(content)
                .member(member)
                .board(board)
                .build();
    }
}
