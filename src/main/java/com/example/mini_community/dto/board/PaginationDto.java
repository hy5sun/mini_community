package com.example.mini_community.dto.board;

import com.example.mini_community.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class PaginationDto {
    private Integer page;
    private Integer size;
    private Integer totalPages;
    private Long totalBoards;
    private Boolean hasPrevious;
    private Boolean hasNext;

    public static PaginationDto entityToDto(Page<Board> boards) {
        return new PaginationDto(boards.getNumber(), boards.getSize(), boards.getTotalPages(), boards.getTotalElements(), boards.hasPrevious(), boards.hasNext());
    }
}
