package com.example.mini_community.dto.board;

import com.example.mini_community.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class BoardResponse {
    private UUID id;
    private String title;
    private String content;
    private String image;
    private String writer;
    private Integer view_count;
    private Integer like_count;

    public static BoardResponse entityToDto(Board board) {
        return new BoardResponse(board.getId(), board.getTitle(), board.getContent(), board.getImage(),
                board.getMember().getNickname(), board.getView_count(), board.getLike_count());
    }
}
