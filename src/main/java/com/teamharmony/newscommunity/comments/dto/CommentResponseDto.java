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
    private String content;
    private UserResponseDto userResponseDto;
    private ProfileResponseDto profileResponseDto;



    @Builder
    public CommentResponseDto(Long commentId,
                              LocalDateTime modifiedAt,
                              String content,
                              UserResponseDto userResponseDto,
                              ProfileResponseDto profileResponseDto) {

        this.commentId = commentId;
        this.modifiedAt = modifiedAt;
        this.content = content;
        this.userResponseDto = userResponseDto;
        this.profileResponseDto = profileResponseDto;
    }

}
