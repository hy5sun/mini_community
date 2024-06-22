package com.example.mini_community.repository.board;

import com.example.mini_community.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
    Optional<Board> findById(UUID id);
    Page<Board> findAll(Pageable pageable);
    Page<Board> findByContentContaining(String keyword, Pageable pageable);
    Page<Board> findByTitleContaining(String keyword, Pageable pageable);
    Page<Board> findByMemberNicknameContaining(String keyword, Pageable pageable);
}
