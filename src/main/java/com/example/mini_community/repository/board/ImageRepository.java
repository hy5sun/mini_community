package com.example.mini_community.repository.board;

import com.example.mini_community.domain.board.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
    Optional<List<Image>> findByBoardId(UUID boardId);
}
