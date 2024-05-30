package com.example.mini_community.dto.board;

import com.example.mini_community.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AllBoardsResponse {
    private String title;
    private String content;
    private String image;

    public static AllBoardsResponse entityToDto(Board board) {
        return new AllBoardsResponse(board.getTitle(), board.getContent(), board.getImage());
    }
}
