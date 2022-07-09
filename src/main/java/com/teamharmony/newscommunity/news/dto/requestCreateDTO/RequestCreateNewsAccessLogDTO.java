package com.teamharmony.newscommunity.news.dto.requestCreateDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestCreateNewsAccessLogDTO {
    /* 뉴스 접근 기록 생성 요청시 사용되는 DTO
    *  생성: /api/news/log의 경로로 전달된 json을 기반해 생성된다.
    *  운용: Service 계층: NewsService의 setNewsAccessLog()에서 로그 생성을 위한 NewsAccessLog의 인자로 들어간다.
    *  */
    private String news_id;             // 사용자가 접근한 newsId
    private String user_id;             // 해당 뉴스에 접근한 userId
    private String title;               // 사용자가 접근한 뉴스의 제목

    @Builder
    public RequestCreateNewsAccessLogDTO(String news_id, String user_id, String title){
        this.news_id = news_id;
        this.user_id = user_id;
        this.title = title;
    }
}