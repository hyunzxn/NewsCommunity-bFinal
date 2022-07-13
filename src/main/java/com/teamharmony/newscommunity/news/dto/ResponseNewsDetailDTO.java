package com.teamharmony.newscommunity.news.dto;


import com.teamharmony.newscommunity.news.entity.NewsTable;
import lombok.Builder;
import lombok.Getter;


/**
 * 뉴스 상세정보를 Response하기 위한 DTO
 * @author hyeoKing
 */
@Getter
public class ResponseNewsDetailDTO {
    private String id;              // 뉴스의 id 정보: Oauth 값을 String 형태로 변환
    private String title;           // 뉴스의 제목 정보
    private String summary;         // python 외부 모듈에서 요약된 뉴스 요약 정보,
    private String image_url;       // html 상의 이미지 표현을 위한 뉴스의 이미지가 저장된 image_url
    private String news_url;        // 해당 뉴스의 원본 주소 (네이버 뉴스의 해당 뉴스 주소)
    private String write_time;      // 작성 시간
    private Long view;              // 조회수

    /**
     * @param newsTable
     */
    @Builder
    public ResponseNewsDetailDTO(NewsTable newsTable){
        this.id = newsTable.getId();
        this.title = newsTable.getTitle();
        this.summary = newsTable.getSummary();
        this.image_url = newsTable.getImage_url();
        this.news_url = newsTable.getNews_url();
        this.write_time = newsTable.getWrite_time();
        this.view = newsTable.getView();
    }
}
