package com.teamharmony.newscommunity.news.dto.createDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestCreateNewsAccessLogDTO {
    private String news_id;
    private String user_id;
    private String title;

    @Builder
    public RequestCreateNewsAccessLogDTO(String news_id, String user_id, String title){
        this.news_id = news_id;
        this.user_id = user_id;
        this.title = title;
    }
}