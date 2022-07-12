package com.teamharmony.newscommunity.news.repository;

import com.teamharmony.newscommunity.news.entity.NewsTable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NewsRepository extends JpaRepository<NewsTable, String> {
}

