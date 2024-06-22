package com.example.mini_community.dto.comment;

import com.example.mini_community.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class CommentDto {
    private UUID id;
    private String writer;
    private String content;
    private Boolean isMine;
    private LocalDateTime createdAt;

    public static CommentDto toDto(Comment comment, Boolean isMine) {
        return new CommentDto(comment.getId(), comment.getMember().getNickname(), comment.getContent(), isMine, comment.getCreatedAt());
    }
}
