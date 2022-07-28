package com.teamharmony.newscommunity.domain.bookmarks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookmarkRequestDto {
    private String newsId;
    private String userId;
    private String title;
}
