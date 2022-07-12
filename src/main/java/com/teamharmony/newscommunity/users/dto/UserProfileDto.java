package com.teamharmony.newscommunity.users.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileDto {
    private String nickname;
    private String profilePic;

    @Builder
    public UserProfileDto(String nickname, String profilePic) {
        this.nickname = nickname;
        this.profilePic = profilePic;
    }
}
