package com.example.mini_community.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardsWithPaginationResponse {
    private List<AllBoardsResponse> boards;
    private PaginationResponse pageInfo;

    public static BoardsWithPaginationResponse entityToDto(List<AllBoardsResponse> boards, PaginationResponse pagination) {
        return new BoardsWithPaginationResponse(boards, pagination);
    }
}
