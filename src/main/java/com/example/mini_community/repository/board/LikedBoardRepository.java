package com.example.mini_community.repository.board;

import com.example.mini_community.domain.board.Board;
import com.example.mini_community.domain.board.LikedBoard;
import com.example.mini_community.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedBoardRepository extends JpaRepository<LikedBoard, Long> {
    Optional<LikedBoard> findByBoardAndMember(Board board, Member member);
}
