package com.teamharmony.newscommunity.news.entity;

import com.teamharmony.newscommunity.news.dto.requestCreateDTO.RequestCreateNewsAccessLogDTO;
import com.teamharmony.newscommunity.news.util.TimestampedOnLog;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;


/**
 * 뉴스에 접근(클릭)에 관련된 기록을 남기기 위한 엔티티
 *
 *  @author : hyeoKing
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewsAccessLog extends TimestampedOnLog {
    /* 뉴스에 접근(클릭)에 관련된 기록을 남기기 위한 엔티티*/

    @Id
    @Column(nullable = false)
    private String id;                      // 접근 기록에 대한 ID

    @Column(nullable = false)
    private String news_id;                 // 접근한 뉴스 ID

    @Column(nullable = false)
    private String user_id;                 // 해당 뉴스에 접근한 userId

    @Column(nullable = false)
    private String title;                   // 접근하 뉴스의 제목

    @Builder
    public NewsAccessLog(RequestCreateNewsAccessLogDTO requestCreateNewsAccessLogDTO){
        this.id = UUID.randomUUID().toString();
        this.news_id = requestCreateNewsAccessLogDTO.getNews_id();
        this.user_id = requestCreateNewsAccessLogDTO.getUser_id();
        this.title = requestCreateNewsAccessLogDTO.getTitle();
    }
}
