package com.example.mini_community.repository.board;

import com.example.mini_community.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
}