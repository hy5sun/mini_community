package com.example.mini_community.service.board;

import com.example.mini_community.common.exception.BusinessException;
import com.example.mini_community.common.type.SearchType;
import com.example.mini_community.domain.board.Board;
import com.example.mini_community.domain.board.Image;
import com.example.mini_community.domain.board.LikedBoard;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.dto.board.*;
import com.example.mini_community.repository.board.BoardRepository;
import com.example.mini_community.repository.board.ImageRepository;
import com.example.mini_community.repository.board.LikedBoardRepository;
import com.example.mini_community.service.S3.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.example.mini_community.common.exception.ErrorCode.*;
import static com.example.mini_community.common.type.SearchType.fromSearchType;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final LikedBoardRepository likedBoardRepository;
    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    private static final int PAGE_SIZE = 10;

    @Transactional
    public BoardResponse createBoard(Member member, CreateBoardRequest req, List<MultipartFile> files) {
        List<Image> images = s3Service.uploadFiles(files);

        Board board = Board.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .images(images)
                .member(member)
                .build();

        boardRepository.save(board);

        images.stream().map(image -> image.setBoardBuilder().board(board));
        imageRepository.saveAll(images);

        return BoardResponse.entityToDto(board);
    }

    @Transactional
    public BoardsWithPaginationResponse findAll(Integer page) {
        Pageable pageable = makePageable(page);
        Page<Board> boards = findAllWithPageable(pageable);
        return makeAllBoardsResponse(boards);
    }

    @Transactional
    public BoardsWithPaginationResponse searchBoard(String type, String keyword, Integer page) {
        Pageable pageable = makePageable(page);
        Page<Board> boards = findByType(type, keyword, pageable);
        return makeAllBoardsResponse(boards);
    }

    private Page<Board> findByType(String type, String keyword, Pageable pageable) {
        SearchType searchType = fromSearchType(type);

        switch (searchType) {
            case TITLE:
                return boardRepository.findByTitleContaining(keyword, pageable);
            case CONTENT:
                return boardRepository.findByContentContaining(keyword, pageable);
            case WRITER:
                return boardRepository.findByMemberNicknameContaining(keyword, pageable);
            default:
                throw new BusinessException(WRONG_SEARCH_TYPE);
        }
    }

    private Pageable makePageable(Integer page) {
        return PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
    }

    private Page<Board> findAllWithPageable(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    private BoardsWithPaginationResponse makeAllBoardsResponse(Page<Board> boards) {
        PaginationDto pageInfo = PaginationDto.entityToDto(boards);

        List<AllBoardsDto> boardsResponse = boards.stream()
                .map(AllBoardsDto::fromEntity)
                .toList();

        return new BoardsWithPaginationResponse(boardsResponse, pageInfo);
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
