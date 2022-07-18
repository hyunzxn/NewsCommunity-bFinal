package com.teamharmony.newscommunity.bookmarks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookmarkRequestDto {
    private String newsId;
    private String userId;
    private String title;
}
