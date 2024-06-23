package com.example.mini_community.controller.member;

import com.example.mini_community.common.annotation.Login;
import com.example.mini_community.common.response.CustomResponse;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.dto.board.BoardsWithPaginationResponse;
import com.example.mini_community.dto.member.MemberDto;
import com.example.mini_community.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    @GetMapping("/boards")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse findMyBoards(@Login Member member, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        BoardsWithPaginationResponse boards = memberService.findBoardByMember(member, page);
        return CustomResponse.response(HttpStatus.OK, "내가 작성한 게시물을 정상적으로 조회했습니다.", boards);
    }

    @GetMapping("/liked")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse findMyLikedBoards(@Login Member member, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        BoardsWithPaginationResponse boards = memberService.findLikedBoardByMember(member, page);
        return CustomResponse.response(HttpStatus.OK, "내가 좋아요한 게시물을 정상적으로 조회했습니다.", boards);
    }

    @GetMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse findMyCommentBoards(@Login Member member, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        BoardsWithPaginationResponse boards = memberService.findCommentBoardByMember(member, page);
        return CustomResponse.response(HttpStatus.OK, "내가 작성한 댓글이 포함된 게시물을 정상적으로 조회했습니다.", boards);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse findMyInformation(@Login Member member) {
        MemberDto memberInfo = memberService.findMyInformation(member);
        return CustomResponse.response(HttpStatus.OK, member.getNickname() + "님의 정보를 정상적으로 조회했습니다.", memberInfo);
    }
}
