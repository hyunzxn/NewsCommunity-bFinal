package com.teamharmony.newscommunity.news.service;

import com.teamharmony.newscommunity.news.dto.createDTO.RequestCreateNewsAccessLogDTO;
import com.teamharmony.newscommunity.news.dto.responseDTO.ResponseNewsDTO;
import com.teamharmony.newscommunity.news.dto.responseDTO.ResponseNewsDetailDTO;
import com.teamharmony.newscommunity.news.entity.NewsAccessLog;
import com.teamharmony.newscommunity.news.entity.NewsTable;
import com.teamharmony.newscommunity.news.repository.NewsAccessLogRepository;
import com.teamharmony.newscommunity.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NewsService {
    /* 뉴스 정보 조회 기능을 수행하는 Service*/
    private final NewsRepository newsRepository;
    private final NewsAccessLogRepository newsAccessLogRepository;

    public ResponseNewsDTO getNews(){
        /* /news의 경로로 요청된 GET 메서드의 응답을 처리하는 메서드, 뉴스정보 리스트를 반환함
         * param: None
         * return: ResponseNewsDTO [뉴스 제목, 요약, 이미지_url, 실제뉴스URL, 설명, 작성시간]
         * */
        List<NewsTable> newsTableList = newsRepository.findAll();
        return ResponseNewsDTO.builder()
                .newsTableList(newsTableList)
                .build();
    }

    public ResponseNewsDetailDTO getNewsDetail(String newsId){
        /*
           /news/detail의 경로로 요청된 GET 메서드의 응답을 처리하는 메서드, 클릭한 news의 id로 뉴스 내용 조회
           param: newsId
           return: ResponseNewsDetailDTO // newsid, title, summary, image_url, news_url, write_time가 담김
         */
        NewsTable newsTable = newsRepository.findById(newsId).orElseThrow(
                ()-> new NullPointerException("아이디가 존재 안함무라이법전")
        );
        return new ResponseNewsDetailDTO(newsTable);
    }

//    public String setNewsAccesLog(RequestCreateNewsAccessLogDTO requestCreateNewsAccessLogDTO){
//        NewsAccessLog newsAccessLog = NewsAccessLog.builder()
//                .requestCreateNewsAccessLogDTO(requestCreateNewsAccessLogDTO)
//                .build();
//        newsAccessLogRepository.save(newsAccessLog);
//        return newsAccessLog.getId();
//    }
}