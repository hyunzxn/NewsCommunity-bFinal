package com.teamharmony.newscommunity.news.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class NewsTable {
    @Id
    @Column(nullable = false, unique = true, length = 30)
    private String id;

    @Column(nullable = false, length = 60)
    private String title;

    @Column(nullable = false, length = 1000)
    private String summary;

    @Column(nullable = false, length = 200)
    private String image_url;

    @Column(nullable = false, length = 200)
    private String news_url;

    @Column(nullable = false, length = 100)
    private String explain;

    @Column(nullable = false, length = 30)
    private String write_time;
}
