package com.teamharmony.newscommunity.news.repository;

import com.teamharmony.newscommunity.news.entity.NewsTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<NewsTable, String> {
    /* 뉴스 정보를 담을 NewsRepository, 아래는 관력 역할
     * GET: 뉴스 정보 리스트, 뉴스 상세 정보 조회:(/api/news)와 (/api/news/details/)
     * POST: 뉴스 접근 기록 생성              : (/api/news/logs)
     * PUT: 조회수 업데이트                   : (/api/news/views/{newsId})
     * DELETE: NULL
     * = 뉴스정보 조회를 위한 역할과 조회수 업데이트를 위한 역할수행
     */
}

