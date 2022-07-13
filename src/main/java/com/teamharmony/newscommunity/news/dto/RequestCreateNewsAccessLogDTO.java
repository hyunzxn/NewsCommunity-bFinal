package com.teamharmony.newscommunity.news.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * NewsAccessLog에 대한 생성 요청을 위해 필요한 DTO
 * @Author Chanhyuk King, the Great East Developer's King
 */
@Getter
@NoArgsConstructor
public class RequestCreateNewsAccessLogDTO {
    private String news_id;
    private String user_id;
    private String title;

    /**
     * @param news_id 뉴스 식별을 위한 id
     * @param user_id 사용자 식별을 위한 id
     * @param title   식별 뉴스의 제목
     */
    @Builder
    public RequestCreateNewsAccessLogDTO(String news_id, String user_id, String title){
        this.news_id = news_id;         // 테스트 코드에서의 객체 생성시, 실수를 줄이기 위해 @Builder 기입
        this.user_id = user_id;
        this.title = title;
    }
}