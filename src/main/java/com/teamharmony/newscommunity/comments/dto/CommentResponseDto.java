package com.teamharmony.newscommunity.comments.dto;

import com.teamharmony.newscommunity.users.dto.ProfileResponseDto;
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
    private Boolean like;



    @Builder
    public CommentResponseDto(Long commentId,
                              LocalDateTime modifiedAt,
                              LocalDateTime createdAt,
                              String content,
                              ProfileResponseDto profileResponseDto,
                              Boolean like) {

        this.commentId = commentId;
        this.modifiedAt = modifiedAt;
        this.createdAt = createdAt;
        this.content = content;
        this.profileResponseDto = profileResponseDto;
        this.like = like;
    }

}
