package com.teamharmony.newscommunity.news.controller;


import com.teamharmony.newscommunity.news.common.ApiResponse;
import com.teamharmony.newscommunity.news.dto.responseDTO.ResponseNewsDTO;
import com.teamharmony.newscommunity.news.dto.responseDTO.ResponseNewsDetailDTO;
import com.teamharmony.newscommunity.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/news")
@RestController
public class NewsController {
    private final NewsService newsService;

    // 1. TODO: 뉴스 정보를 일괄 반환하는 /api/news에 대한 GET 요청  //DONE
    @GetMapping("")
    public ApiResponse<ResponseNewsDTO> getNews(){
        /* index.html에서 뉴스 조회 요청시 DB에 담긴 전체 뉴스 리스트를(ResponseNewsDTO) 리턴
            param: None
            return: 뉴스정보(NewsTable)리스트가 담긴 ResponseNewsDTO를 리턴
         */
        //return ResponseEntity.status(HttpStatus.CREATED).body(newsService.getNews()); // 학습을 위한 응답 사례 비교 코드
        return ApiResponse.success("result", newsService.getNews());
    }

    // 2. TODO: 해당 newsId의 상세뉴스정보를 반환하는 /detail/{newsId}에 대한 GET요청  //DONE
    @GetMapping("/details/{newsId}")
    public ApiResponse<ResponseNewsDetailDTO> getNewsDetial(@PathVariable String newsId){
        /*  detail.html에서 해당 뉴스의 id 값을 통해 Get요청을 보내는데, 해당 id의 뉴스 정보를 리턴해주는 역할을 한다.
            param: newsId // 해당 뉴스의 id
            return: 뉴스정보가 담긴 ResponseNewsDetailDTO
        */
        return ApiResponse.success("result", newsService.getNewsDetail(newsId));
    }

}
