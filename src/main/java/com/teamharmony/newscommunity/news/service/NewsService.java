package com.teamharmony.newscommunity.news.service;

import com.teamharmony.newscommunity.news.dto.responseDTO.ResponseNewsDTO;
import com.teamharmony.newscommunity.news.entity.NewsTable;
import com.teamharmony.newscommunity.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NewsService {
    private final NewsRepository newsRepository;

    public ResponseNewsDTO getNews(){
        List<NewsTable> newsTableList = newsRepository.findAll();
        return ResponseNewsDTO.builder()
                                .newsTableList(newsTableList)
                                .build();
    }
}
