package com.teamharmony.newscommunity.news.repository;

import com.teamharmony.newscommunity.news.entity.NewsAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsAccessLogRepository extends JpaRepository<NewsAccessLog, String> {
    /* 뉴스 접근 정보를 로깅할 NewsRepository,
     * GET: NULL
     * POST: /api/news/log를 통해 userId와 newsId가 들어오면 NewsAccessLog를 생성
     * PUT: NULL
     * DELETE: NULL
     * = 조회를 위한 역할만 수행(/news[GET] from NewsController-NewsService)
     * */
}