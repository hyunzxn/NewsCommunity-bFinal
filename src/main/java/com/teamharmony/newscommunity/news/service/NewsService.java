package com.teamharmony.newscommunity.news.service;

import com.teamharmony.newscommunity.news.dto.requestCreateDTO.RequestCreateNewsAccessLogDTO;
import com.teamharmony.newscommunity.news.dto.responseDTO.ResponseNewsDTO;
import com.teamharmony.newscommunity.news.dto.responseDTO.ResponseNewsDetailDTO;
import com.teamharmony.newscommunity.news.entity.NewsAccessLog;
import com.teamharmony.newscommunity.news.entity.NewsTable;
import com.teamharmony.newscommunity.news.repository.NewsAccessLogRepository;
import com.teamharmony.newscommunity.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


/**
 *  뉴스 정보 조회 기능을 수행하는 Service
 * @author hyeoKing
 */
@RequiredArgsConstructor
@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsAccessLogRepository newsAccessLogRepository;
    /**
     * /api/news의 경로로 요청된 GET 메서드의 응답을 처리하는 메서드
     * @param: None
     * @Return: ResponseNewsDTO [뉴스 제목, 요약, 이미지_url, 실제뉴스URL, 설명, 작성시간]
     **/
    public ResponseNewsDTO getNews(){
        List<NewsTable> newsTableList = newsRepository.findAll();
        return ResponseNewsDTO.builder()
                .newsTableList(newsTableList)
                .build();
    }

    /** /api/news/details의 경로로 요청된 GET 메서드의 응답을 처리하는 메서드, news의 id로 뉴스 내용 조회
     * @param: newsId
     * @Return: ResponseNewsDetailDTO // newsid, title, summary, image_url, news_url, write_time가 담김
     **/
    public ResponseNewsDetailDTO getNewsDetail(String newsId){

        NewsTable newsTable = newsRepository.findById(newsId).orElseThrow(
                ()-> new NullPointerException("해당 뉴스 id가 존재 안합니다.")
        );
        return new ResponseNewsDetailDTO(newsTable);
    }

    /**
    /api/news/logs의 경로로 요청된 POST 메서드의 응답을 처리하는 메서드, 클릭한 news의 id와 유저 id로 접근
    @param: requestCreateNewsAccessLogDTO // newsId, userId 정보가 담겨옴
    @Return: newsAccessLog.getNews_Id()        // 생성된 뉴스로그에 담긴 뉴스 id를 리턴 **/
    public String setNewsAccessLog(RequestCreateNewsAccessLogDTO requestCreateNewsAccessLogDTO){
        NewsAccessLog newsAccessLog = NewsAccessLog.builder()
                .requestCreateNewsAccessLogDTO(requestCreateNewsAccessLogDTO)
                .build();                               // newsAccessLog를 requestCreateNewsAccessLogDTO로 초기화
        newsAccessLogRepository.save(newsAccessLog);    // newsAccessLogRepository를 통해 newsAccessRepository에 newsAccessLog 저장
        return newsAccessLog.getNews_id();
    }

    /**
     * /api/news/view의 경로로 요청된 메서드의 응답을 처리하는 함수, 해당 newsId의 view 컬럼의 값을 1 추가
     * @param newsId
     */
    @Transactional
    public void addView(String newsId) {
        NewsTable newsTable = newsRepository.findById(newsId).orElseThrow(
                ()-> new NullPointerException("해당 뉴스가 존재 안합니다.")
        );
        newsTable.updateView();
        newsRepository.save(newsTable);
    }
}