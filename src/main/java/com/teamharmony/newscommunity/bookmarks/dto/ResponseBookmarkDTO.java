package com.teamharmony.newscommunity.bookmarks.dto;

import com.teamharmony.newscommunity.bookmarks.entity.Bookmarks;
import com.teamharmony.newscommunity.users.dto.UserResponseDto;
import com.teamharmony.newscommunity.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;


@Getter
public class ResponseBookmarkDTO {
    String id;          // 북마크 식별을 위한 id
    String newsId;      // 사용자가 접근한 newsId
    String userId;      // 해당 뉴스에 접근한 userId
    String title;

    @Builder
    public ResponseBookmarkDTO(String id, String newsId, String userId, String title){
        this.id = id;
        this.newsId = newsId;
        this.userId = userId;
        this.title = title;
    }
    public static ResponseBookmarkDTO toDto(Bookmarks bookmarks) { return ResponseBookmarkDTO.builder().id(bookmarks.getNewsId())
                                                                                                        .newsId(bookmarks.getNewsId())
                                                                                                        .userId(bookmarks.getUserId())
                                                                                                        .title(bookmarks.getTitle())
                                                                                                        .build();}
}
