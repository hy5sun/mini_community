package com.example.mini_community.domain.board;

import com.example.mini_community.common.entity.BaseTimeEntity;
import com.example.mini_community.domain.comment.Comment;
import com.example.mini_community.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
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

    @OneToMany(mappedBy = "board")
    private List<Image> images;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer view_count;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer like_count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<LikedBoard> likedBoards;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @Builder
    public Board(String title, String content, List<Image> images, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.images = images;
        this.like_count = 0;
        this.view_count = 0;
    }

    public void update(String title, String content, List<Image> images) {
        this.title = title;
        this.content = content;
        this.images = images;
    }

    public void increaseViewCount() {
        this.view_count++;
    }

    public void increaseLikeCount() {
        this.like_count++;
    }

    public void decreaseLikeCount() {
        this.like_count--;
    }
}
