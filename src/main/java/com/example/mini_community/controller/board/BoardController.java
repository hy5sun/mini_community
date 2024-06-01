package com.example.mini_community.controller.board;

import com.example.mini_community.common.config.annotation.Login;
import com.example.mini_community.common.response.CustomResponse;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.dto.board.AllBoardsResponse;
import com.example.mini_community.dto.board.BoardResponse;
import com.example.mini_community.dto.board.CreateBoardRequest;
import com.example.mini_community.dto.board.UpdateBoardRequest;
import com.example.mini_community.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public CustomResponse findAll(@Login Member member) {
        List<AllBoardsResponse> boards = boardService.findAll();

        return CustomResponse.response(HttpStatus.OK, "모든 게시물을 정상적으로 조회했습니다.", boards);
    }

    @GetMapping("/{boardId}")
    public CustomResponse findById(@PathVariable("boardId") UUID boardId, @Login Member member) {
        BoardResponse board = boardService.findById(boardId);

        return CustomResponse.response(HttpStatus.OK, "게시물을 정상적으로 조회했습니다.", board);
    }

    @PutMapping("/{boardId}")
    public CustomResponse editBoard(@PathVariable("boardId") UUID boardId, @Validated @RequestBody UpdateBoardRequest req, @Login Member member) {
        BoardResponse board = boardService.editBoard(boardId, req, member);

        return CustomResponse.response(HttpStatus.OK, "게시물을 정상적으로 수정했습니다.", board);
    }

}
