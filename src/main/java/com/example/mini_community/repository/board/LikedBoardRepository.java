package com.example.mini_community.repository.board;

import com.example.mini_community.domain.board.LikedBoard;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LikedBoardRepository extends JpaRepository<LikedBoard, Long> {
}
