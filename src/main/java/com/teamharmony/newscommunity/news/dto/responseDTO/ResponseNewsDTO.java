package com.teamharmony.newscommunity.news.dto.responseDTO;
import com.teamharmony.newscommunity.news.entity.NewsTable;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
public class ResponseNewsDTO {
    private List<NewsTable> newsTableList;
    @Builder
    public ResponseNewsDTO(List newsTableList){
        this.newsTableList = newsTableList;
    }
}
