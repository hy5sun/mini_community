package com.example.mini_community.dto.board;

import com.example.mini_community.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AllBoardsDto {
    private String id;
    private String title;
    private String content;
    private String thumbnail;
    private String writer;
    private LocalDateTime createdAt;

    public static AllBoardsDto fromEntity(Board board) {
        return new AllBoardsDto(
                board.getId().toString(),
                board.getTitle(),
                board.getContent(),
                board.getImages().get(0).getImageUrl(),
                board.getMember().getNickname(),
                board.getCreatedAt());
    }
}
