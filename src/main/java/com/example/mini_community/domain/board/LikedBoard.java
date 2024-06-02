package com.example.mini_community.domain.board;

import com.example.mini_community.common.entity.BaseTimeEntity;
import com.example.mini_community.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Table(name="likedBoard")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikedBoard extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Boolean isLiked = true;

    @Builder
    public LikedBoard(Board board, Member member) {
        this.board = board;
        this.member = member;
        this.isLiked = true;
    }
}
