package com.example.mini_community.service.board;

import com.example.mini_community.common.exception.BusinessException;
import com.example.mini_community.common.type.SearchType;
import com.example.mini_community.domain.board.Board;
import com.example.mini_community.domain.board.Image;
import com.example.mini_community.domain.board.LikedBoard;
import com.example.mini_community.domain.comment.Comment;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.dto.board.*;
import com.example.mini_community.repository.board.BoardRepository;
import com.example.mini_community.repository.board.ImageRepository;
import com.example.mini_community.repository.board.LikedBoardRepository;
import com.example.mini_community.repository.comment.CommentRepository;
import com.example.mini_community.service.S3.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.mini_community.common.exception.ErrorCode.*;
import static com.example.mini_community.common.type.SearchType.fromSearchType;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final LikedBoardRepository likedBoardRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
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

        images.stream().forEach(image -> image.setBoard(board));
        imageRepository.saveAll(images);

        return BoardResponse.entityToDto(board, isLiked(board, member));
    }

    @Transactional
    public BoardsWithPaginationResponse findAll(Integer page) {
        Pageable pageable = makePageable(page);
        Page<Board> boards = boardRepository.findAll(pageable);
        return makeAllBoardsResponse(boards);
    }

    @Transactional
    public BoardsWithPaginationResponse findBoardByMember(Member member, Integer page) {
        Pageable pageable = makePageable(page);
        Page<Board> boards = boardRepository.findByMember(member, pageable);
        return makeAllBoardsResponse(boards);
    }

    @Transactional
    public BoardsWithPaginationResponse findLikedBoardByMember(Member member, Integer page) {
        Pageable pageable = makePageable(page);
        Page<Board> boards = likedBoardRepository.findByMember(member, pageable).map(LikedBoard::getBoard);
        return makeAllBoardsResponse(boards);
    }

    @Transactional
    public BoardsWithPaginationResponse findCommentBoardByMember(Member member, Integer page) {
        Pageable pageable = makePageable(page);
        List<Board> distinctBoards = commentRepository.findByMember(member, pageable).map(Comment::getBoard).stream().distinct().collect(Collectors.toList());
        Page<Board> commentBoardsPage = new PageImpl<>(distinctBoards, pageable, distinctBoards.size());
        return makeAllBoardsResponse(commentBoardsPage);
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
        return BoardResponse.entityToDto(board, isLiked(board, member));
    }

    @Transactional
    public BoardResponse editBoard(UUID id, UpdateBoardRequest req, List<MultipartFile> files, Member member) {
        Board board = this.getById(id);
        validateAuthor(board, member);

        deleteImagesByBoard(board);

        List<Image> newImages = s3Service.uploadFiles(files);
        newImages.stream().forEach(image -> image.setBoard(board));
        imageRepository.saveAll(newImages);

        board.update(req.getTitle(), req.getContent(), newImages);
        return BoardResponse.entityToDto(board, isLiked(board, member));
    }

    private void deleteImagesByBoard(Board board) {
        List<Image> existingImages = imageRepository.findByBoard(board)
                .orElseThrow(() -> new BusinessException(IMAGE_NOT_FOUND));

        imageRepository.deleteByBoard(board);
        existingImages.stream().map(Image::getSavedName).forEach(s3Service::deleteFile);
    }

    @Transactional
    public void deleteBoard(UUID id, Member member) {
        Board board = getById(id);
        validateAuthor(board, member);
        deleteImagesByBoard(board);
        boardRepository.delete(board);
    }


    @Transactional
    public BoardLikeResponse updateLikeCount(UUID id, Member member) {
        Board board = getById(id);

        if (isAuthor(board, member)) {
            throw new BusinessException(BAD_REQUEST);
        }

        if (!isLiked(board, member)) {
            increaseLikeCount(board, member);
        } else {
            decreaseLikeCount(board, member);
        }

        return BoardLikeResponse.entityToDto(board);
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
