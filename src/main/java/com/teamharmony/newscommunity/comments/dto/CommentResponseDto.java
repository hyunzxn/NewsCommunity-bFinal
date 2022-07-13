package com.teamharmony.newscommunity.comments.dto;

import com.teamharmony.newscommunity.users.dto.response.ProfileResponseDto;
import com.teamharmony.newscommunity.users.dto.response.UserResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long commentId;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;
    private String content;
    private ProfileResponseDto profileResponseDto;



    @Builder
    public CommentResponseDto(Long commentId,
                              LocalDateTime modifiedAt,
                              LocalDateTime createdAt,
                              String content,
                              ProfileResponseDto profileResponseDto) {

        this.commentId = commentId;
        this.modifiedAt = modifiedAt;
        this.createdAt = createdAt;
        this.content = content;
        this.profileResponseDto = profileResponseDto;
    }

}
