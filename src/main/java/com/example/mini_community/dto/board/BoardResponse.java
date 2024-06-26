package com.example.mini_community.dto.board;

import com.example.mini_community.domain.board.Board;
import com.example.mini_community.domain.board.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class BoardResponse {
    private UUID id;
    private String title;
    private String content;
    private List<String> imageUrl;
    private String writer;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean isLike;

    public static BoardResponse entityToDto(Board board, Boolean isLike) {
        Integer commentCount = board.getComments() == null ? 0 : board.getComments().size();

        return new BoardResponse(board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getImages().stream().map(Image::getImageUrl).collect(Collectors.toList()),
                board.getMember().getNickname(),
                board.getView_count(),
                board.getLike_count(),
                commentCount,
                isLike);
    }
}
