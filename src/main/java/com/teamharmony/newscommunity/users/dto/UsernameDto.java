package com.teamharmony.newscommunity.users.dto;

import lombok.Getter;

@Getter
public class UsernameDto {
    private String username;

    public UsernameDto (String username) {
        this.username = username;
    }
}
