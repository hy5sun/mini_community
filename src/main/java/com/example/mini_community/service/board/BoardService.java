package com.example.mini_community.service.board;

import com.example.mini_community.common.exception.BusinessException;
import com.example.mini_community.domain.board.Board;
import com.example.mini_community.domain.board.LikedBoard;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.dto.board.AllBoardsResponse;
import com.example.mini_community.dto.board.BoardResponse;
import com.example.mini_community.dto.board.CreateBoardRequest;
import com.example.mini_community.dto.board.UpdateBoardRequest;
import com.example.mini_community.repository.board.BoardRepository;
import com.example.mini_community.repository.board.LikedBoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.example.mini_community.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    private LikedBoardRepository likedBoardRepository;

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
                .map((Board board) -> new AllBoardsResponse(board.getId().toString(), board.getTitle(), board.getContent(), board.getImage(), board.getMember().getNickname()))
                .toList();

        return boards;
    }

    @Transactional
    public BoardResponse findById(UUID id, Member member) {
        Board board = getById(id);
        increaseViewCount(board, member);
        return BoardResponse.entityToDto(board);
    }

    @Transactional
    public BoardResponse editBoard(UUID id, UpdateBoardRequest req, Member member) {
        Board board = this.getById(id);
        validateAuthor(board, member);
        board.update(req.getTitle(), req.getContent(), req.getImage());
        return BoardResponse.entityToDto(board);
    }

    @Transactional
    public BoardResponse deleteBoard(UUID id, Member member) {
        Board board = getById(id);
        validateAuthor(board, member);
        boardRepository.delete(board);
        return BoardResponse.entityToDto(board);
    }

    @Transactional
    public BoardResponse updateLikeCount(UUID id, Member member) {
        Board board = getById(id);

        if (isAuthor(board, member)) {
            throw new BusinessException(BAD_REQUEST);
        }

        if (!isLiked(board, member)) {
            increaseLikeCount(board, member);
        } else {
            decreaseLikeCount(board, member);
        }

        return BoardResponse.entityToDto(board);
    }

    public void increaseLikeCount(Board board, Member member) {
        LikedBoard likedBoard = LikedBoard.builder()
                .board(board)
                .member(member)
                .build();
        likedBoardRepository.save(likedBoard);
        board.increaseLikeCount();
    }

    public void decreaseLikeCount(Board board, Member member) {
        LikedBoard likedBoard = likedBoardRepository.findByBoardAndMember(board, member)
                .orElseThrow(() -> new BusinessException(LIKED_BOARD_NOT_FOUND));
        board.decreaseLikeCount();
        likedBoardRepository.delete(likedBoard);
    }

    public Board getById(UUID id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BOARD_NOT_FOUND));
    }

    public Boolean isAuthor(Board board, Member member) {
        return member.equals(board.getMember());
    }

    public void validateAuthor(Board board, Member member) {
        if (!isAuthor(board, member)) {
            throw new BusinessException(UNAUTHORIZED_MEMBER);
        }
    }

    public void increaseViewCount(Board board, Member member) {
        if (!isAuthor(board, member)) {
            board.increaseViewCount();
        }
    }

    private Boolean isLiked(Board board, Member member) {
        return likedBoardRepository.findByBoardAndMember(board, member)
                .isPresent();
    }
}
