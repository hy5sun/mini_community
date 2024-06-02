package com.example.mini_community.controller.board;

import com.example.mini_community.common.config.annotation.Login;
import com.example.mini_community.common.response.CustomResponse;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.dto.board.*;
import com.example.mini_community.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping()
    public CustomResponse create(@Validated @RequestBody CreateBoardRequest req, @Login Member member) {
        BoardResponse board = boardService.createBoard(member, req);

        return CustomResponse.response(HttpStatus.CREATED, "게시물을 정상적으로 작성했습니다.", board);
    }

    @GetMapping()
    public CustomResponse findAll(@Login Member member, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        BoardsWithPaginationResponse boards = boardService.findAll(page);

        return CustomResponse.response(HttpStatus.OK, "모든 게시물을 정상적으로 조회했습니다.", boards);
    }

    @GetMapping("/{boardId}")
    public CustomResponse findById(@PathVariable("boardId") UUID boardId, @Login Member member) {
        BoardResponse board = boardService.findById(boardId, member);

        return CustomResponse.response(HttpStatus.OK, "게시물을 정상적으로 조회했습니다.", board);
    }

    @PutMapping("/{boardId}")
    public CustomResponse editBoard(@PathVariable("boardId") UUID boardId, @Validated @RequestBody UpdateBoardRequest req, @Login Member member) {
        BoardResponse board = boardService.editBoard(boardId, req, member);

        return CustomResponse.response(HttpStatus.OK, "게시물을 정상적으로 수정했습니다.", board);
    }

    @DeleteMapping("/{boardId}")
    public CustomResponse deleteBoard(@PathVariable("boardId") UUID boardId, @Login Member member) {
        BoardResponse board = boardService.deleteBoard(boardId, member);

        return CustomResponse.response(HttpStatus.OK, "게시물이 정상적으로 삭제되었습니다.", board);
    }

    @PostMapping("/{boardId}")
    public CustomResponse increaseLike(@PathVariable("boardId") UUID boardId, @Login Member member) {
        BoardResponse board = boardService.updateLikeCount(boardId, member);
        return CustomResponse.response(HttpStatus.OK, "좋아요를 눌렀습니다.", board);
    }
}
