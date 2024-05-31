package com.example.mini_community.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AllBoardsResponse {
    private String id;
    private String title;
    private String content;
    private String image;
    private String writer;
}
