package com.example.mini_community.dto.board;

import com.example.mini_community.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoardLikeResponse {
    private Integer likeCount;

    public static BoardLikeResponse entityToDto(Board board) {
        return new BoardLikeResponse(board.getLike_count());
    }
}
