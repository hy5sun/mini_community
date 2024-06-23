package com.example.mini_community.repository.comment;

import com.example.mini_community.domain.comment.Comment;
import com.example.mini_community.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Optional<Comment> findById(UUID id);
    List<Comment> findByBoardIdOrderByCreatedAtAsc(UUID id);
    Page<Comment> findByMember(Member member, Pageable pageable);
}
