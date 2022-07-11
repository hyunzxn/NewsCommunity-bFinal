package com.teamharmony.newscommunity.news.controller;


import com.teamharmony.newscommunity.common.ApiResponse;
import com.teamharmony.newscommunity.news.dto.requestCreateDTO.RequestCreateNewsAccessLogDTO;
import com.teamharmony.newscommunity.news.dto.responseDTO.ResponseNewsDTO;
import com.teamharmony.newscommunity.news.dto.responseDTO.ResponseNewsDetailDTO;
import com.teamharmony.newscommunity.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    // 3. TODO: 뉴스 접근 기록을 저장하는 log에 대한 GET 요청, 조회  //DONE
    @PostMapping("/logs")
    public ApiResponse<String> createNewsAccessLog(@RequestBody RequestCreateNewsAccessLogDTO requestCreateNewsAccessLogDTO){
        /*  사용자가 뉴스 preview를 클릭해 detail 페이지에 접근할 때마다 기록 작성
            param: form('news_id', 'user_id', 'news_title')을 RequestCreateNewsAccessLogDTO에 담아 전달
            return: 로그를 작성한 뉴스의 id
         */
        String newsId = newsService.setNewsAccessLog(requestCreateNewsAccessLogDTO);
        return ApiResponse.success("result", newsId);
    }

    //4. TODO: 뉴스 조회수 업데이트 요청  //DONE
    @PutMapping("/views/{newsId}")
    public ApiResponse<String> UpdateView(@PathVariable String newsId){
        /*  detail.html에 유저 접근시 해당 뉴스의 조회수를 올려주기 위한 흐름
            param: newsTable에서 해당 뉴스의 view를 조회하기 위해 newsId를 받아옴
            return: view를 ApiResponse에 담아 보냄
         */
        newsService.addView(newsId);
        return ApiResponse.success("result", "success");
    }

}
