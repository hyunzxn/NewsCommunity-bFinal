package com.teamharmony.newscommunity.news.entity;

import com.teamharmony.newscommunity.news.dto.createDTO.RequestCreateNewsAccessLogDTO;
import com.teamharmony.newscommunity.news.util.TimestampedOnLog;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewsAccessLog extends TimestampedOnLog {
    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String news_id;

    @Column(nullable = false)
    private String user_id;

    @Column(nullable = false)
    private String title;

    @Builder
    public NewsAccessLog(RequestCreateNewsAccessLogDTO requestCreateNewsAccessLogDTO){
        this.id = UUID.randomUUID().toString();
        this.news_id = requestCreateNewsAccessLogDTO.getNews_id();
        this.user_id = requestCreateNewsAccessLogDTO.getUser_id();
        this.title = requestCreateNewsAccessLogDTO.getTitle();
    }
}
