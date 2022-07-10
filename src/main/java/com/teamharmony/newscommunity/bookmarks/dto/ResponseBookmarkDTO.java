package com.teamharmony.newscommunity.bookmarks.dto;

import com.teamharmony.newscommunity.bookmarks.entity.Bookmarks;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
public class ResponseBookmarkDTO {
    List<Bookmarks> bookmarksList;

    @Builder
    public ResponseBookmarkDTO(List<Bookmarks> bookmarksList){
        this.bookmarksList = bookmarksList;
    }
}
