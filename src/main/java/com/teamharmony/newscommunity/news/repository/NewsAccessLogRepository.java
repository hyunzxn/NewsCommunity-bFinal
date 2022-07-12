package com.teamharmony.newscommunity.news.repository;

import com.teamharmony.newscommunity.news.entity.NewsAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NewsAccessLogRepository extends JpaRepository<NewsAccessLog, String> {
}