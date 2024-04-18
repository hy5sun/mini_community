package com.example.mini_community.dto.auth;

import com.example.mini_community.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class JoinResponse {
    private Integer statusCode;
    private Data data;

    @AllArgsConstructor
    @Getter
    private static class Data {
        private String message;
        private Long id;
        private String email;
        private String nickname;
        private String profileImg;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    public static JoinResponse entityToDto(Integer statusCode, String message, Member member) {
        return new JoinResponse(statusCode, new Data(message, member.getId(), member.getEmail(), member.getNickname(), member.getProfile_img(), member.getCreatedAt(), member.getUpdatedAt()));
    }
}
