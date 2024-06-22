package com.example.mini_community.controller.board;

import com.example.mini_community.common.annotation.Login;
import com.example.mini_community.common.response.CustomResponse;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.dto.board.*;
import com.example.mini_community.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CustomResponse create(@Validated @RequestPart("board") CreateBoardRequest req, @RequestPart(value = "files") List<MultipartFile> files, @Login Member member) {
        BoardResponse board = boardService.createBoard(member, req, files);

        return CustomResponse.response(HttpStatus.CREATED, "게시물을 정상적으로 작성했습니다.", board);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse findAll(@Login Member member, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        BoardsWithPaginationResponse boards = boardService.findAll(page);

        return CustomResponse.response(HttpStatus.OK, "모든 게시물을 정상적으로 조회했습니다.", boards);
    }

    @GetMapping("/{boardId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse findById(@PathVariable("boardId") UUID boardId, @Login Member member) {
        BoardResponse board = boardService.findById(boardId, member);

        return CustomResponse.response(HttpStatus.OK, "게시물을 정상적으로 조회했습니다.", board);
    }

    @PutMapping("/{boardId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse editBoard(@PathVariable("boardId") UUID boardId, @Validated @RequestPart("board") UpdateBoardRequest req, @RequestPart(value = "files") List<MultipartFile> files, @Login Member member) {
        BoardResponse board = boardService.editBoard(boardId, req, files, member);

        return CustomResponse.response(HttpStatus.OK, "게시물을 정상적으로 수정했습니다.", board);
    }

    @DeleteMapping("/{boardId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse deleteBoard(@PathVariable("boardId") UUID boardId, @Login Member member) {
        BoardResponse board = boardService.deleteBoard(boardId, member);

        return CustomResponse.response(HttpStatus.OK, "게시물이 정상적으로 삭제되었습니다.", board);
    }

    @PostMapping("/{boardId}/like")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse increaseLike(@PathVariable("boardId") UUID boardId, @Login Member member) {
        BoardLikeResponse board = boardService.updateLikeCount(boardId, member);
        return CustomResponse.response(HttpStatus.OK, "좋아요를 눌렀습니다.", board);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse search(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam("keyword") String keyword, @RequestParam("type") String type, @Login Member member) {
        BoardsWithPaginationResponse boards = boardService.searchBoard(type, keyword, page);
        return CustomResponse.response(HttpStatus.OK, "검색을 성공했습니다.", boards);
    }
}
