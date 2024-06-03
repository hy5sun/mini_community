package com.example.mini_community.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardsWithPaginationResponse {
    private List<AllBoardsDto> boards;
    private PaginationDto pageInfo;

    public static BoardsWithPaginationResponse entityToDto(List<AllBoardsDto> boards, PaginationDto pagination) {
        return new BoardsWithPaginationResponse(boards, pagination);
    }
}
