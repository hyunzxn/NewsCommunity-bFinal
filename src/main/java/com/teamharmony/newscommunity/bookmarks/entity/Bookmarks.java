package com.teamharmony.newscommunity.bookmarks.entity;


import com.teamharmony.newscommunity.bookmarks.dto.RequestBookmarkDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Bookmarks extends TimestampedOnBookmark {
    @Id
    @Column(nullable = false, unique = true, length = 60)
    String id;          // 북마크 식별을 위한 id

    @Column(nullable = false, length=70)
    String newsId;      // 사용자가 접근한 newsId

    @Column(nullable = false, length = 50)
    String userId;      // 해당 뉴스에 접근한 userId

    @Builder
    public Bookmarks(RequestBookmarkDTO requestBookmarkDTO){
        this.id = UUID.randomUUID().toString();
        this.newsId = requestBookmarkDTO.getNewsId();
        this.userId = requestBookmarkDTO.getUserId();
    }
}
