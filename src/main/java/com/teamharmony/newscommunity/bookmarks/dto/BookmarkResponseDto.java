package com.teamharmony.newscommunity.bookmarks.dto;

import com.teamharmony.newscommunity.bookmarks.entity.Bookmarks;
import lombok.Builder;
import lombok.Getter;


@Getter
public class BookmarkResponseDto {
    String id;          // 북마크 식별을 위한 id
    String newsId;      // 사용자가 접근한 newsId
    String userId;      // 해당 뉴스에 접근한 userId
    String title;

    @Builder
    public BookmarkResponseDto(String id, String newsId, String userId, String title){
        this.id = id;
        this.newsId = newsId;
        this.userId = userId;
        this.title = title;
    }
    public static BookmarkResponseDto toDto(Bookmarks bookmarks) { return BookmarkResponseDto.builder().id(bookmarks.getNewsId())
                                                                                                        .newsId(bookmarks.getNewsId())
                                                                                                        .userId(bookmarks.getUserId())
                                                                                                        .title(bookmarks.getTitle())
                                                                                                        .build();}
}
