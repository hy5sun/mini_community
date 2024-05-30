package com.example.mini_community.service.board;

import com.example.mini_community.domain.board.Board;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.dto.board.AllBoardsResponse;
import com.example.mini_community.dto.board.BoardResponse;
import com.example.mini_community.dto.board.CreateBoardRequest;
import com.example.mini_community.repository.board.BoardRepository;
import com.example.mini_community.service.member.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public BoardResponse createBoard(Member member, CreateBoardRequest req) {
        Board board = Board.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .image(req.getImage())
                .member(member)
                .build();

        boardRepository.save(board);

        return BoardResponse.entityToDto(board);
    }

    @Transactional
    public List<AllBoardsResponse> findAll() {
        // TODO : 추후에 category 별로 조회하도록 할 예정

        List<AllBoardsResponse> boards = boardRepository.findAll()
                .stream()
                .map((Board board) -> new AllBoardsResponse(board.getTitle(), board.getContent(), board.getImage()))
                .toList();

        return boards;
    }
}
