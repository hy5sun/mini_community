package com.example.mini_community.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class RefreshToken {
    @Id
    @Column(name="member_id", nullable = false)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name="member_id")
    private Member member;

    @Column(nullable = false)
    private String refreshToken;

    @Builder
    public RefreshToken(Member member, String refreshToken) {
        this.member = member;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(String newToken) {
        this.refreshToken = newToken;
        return this;
    }
}
