package com.example.mini_community.service.comment;

import com.example.mini_community.domain.comment.Comment;
import com.example.mini_community.dto.comment.CommentDto;
import com.example.mini_community.dto.comment.CreateCommentRequest;
import com.example.mini_community.dto.comment.UpdateCommentRequest;
import com.example.mini_community.repository.comment.CommentRepository;
import com.example.mini_community.common.exception.BusinessException;
import com.example.mini_community.domain.board.Board;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.service.board.BoardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.mini_community.common.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final BoardService boardService;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentDto createComment(UUID boardId, CreateCommentRequest req, Member member) {
        Board board = boardService.getById(boardId);
        Comment comment = commentRepository.save(req.toEntity(member, board));
        return CommentDto.toDto(comment, true);
    }

    @Transactional
    public List<CommentDto> findAllByBoardId(UUID boardId, Member member) {
        return commentRepository.findByBoardIdOrderByCreatedAtAsc(boardId).stream()
                .map((Comment comment) -> CommentDto.toDto(comment, isMine(comment, member)))
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto editComment(UUID boardId, UUID commentId, UpdateCommentRequest req, Member member) {
        validateBoardExist(boardId);
        Comment comment = findById(commentId);
        validateAuthor(comment, member);
        comment.update(req.getContent());
        return CommentDto.toDto(comment, isMine(comment, member));
    }

    @Transactional
    public void deleteComment(UUID boardId, UUID commentId, Member member) {
        validateBoardExist(boardId);
        Comment comment = findById(commentId);
        validateAuthor(comment, member);
        commentRepository.delete(comment);
    }


    public Comment findById(UUID id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(COMMENT_NOT_FOUND));
    }

    private void validateAuthor(Comment comment, Member member) {
        if (!member.equals(comment.getMember())) {
            throw new BusinessException(UNAUTHORIZED_MEMBER);
        }
    }

    private Boolean isMine(Comment comment, Member member) {
        return member.equals(comment.getMember());
    }

    private void validateBoardExist(UUID boardId) {
        boardService.getById(boardId);
    }
}
