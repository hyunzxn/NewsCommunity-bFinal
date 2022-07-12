package com.teamharmony.newscommunity.comments.dto;

import com.teamharmony.newscommunity.users.dto.UserProfileDto;
import com.teamharmony.newscommunity.users.dto.UsernameDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long commentId;
    private LocalDateTime modifiedAt;
    private String content;

    // User 관련된 Dto를 이렇게 쓰는 것이 맞는것인가?
    private UsernameDto usernameDto;
    private UserProfileDto userProfileDto;



    @Builder
    public CommentResponseDto(Long commentId, LocalDateTime modifiedAt, String content,
                              UsernameDto usernameDto, UserProfileDto userProfileDto) {
        this.commentId = commentId;
        this.modifiedAt = modifiedAt;
        this.content = content;
        this.usernameDto = usernameDto;
        this.userProfileDto = userProfileDto;
    }

}
