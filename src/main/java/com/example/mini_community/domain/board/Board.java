package com.example.mini_community.domain.board;

import com.example.mini_community.common.entity.BaseTimeEntity;
import com.example.mini_community.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Table(name="board")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String image;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer view_count;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer like_count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @Builder
    public Board(String title, String content, Member member, String image) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.image = image;
        this.like_count = 0;
        this.view_count = 0;
    }

    public void update(String title, String content, String image) {
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public void increaseViewCount() {
        this.view_count++;
    }


}
