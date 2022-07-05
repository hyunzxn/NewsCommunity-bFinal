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

    @GetMapping("")
    public ApiResponse<ResponseNewsDTO> getNews(){
        /* index.html에서 뉴스 조회 요청시 DB에 담긴 전체 뉴스 리스트를(ResponseNewsDTO) 리턴
            param: None
            return: 뉴스정보(NewsTable)리스트가 담긴 ResponseNewsDTO를 리턴
         */
//      return ResponseEntity.status(HttpStatus.CREATED).body(newsService.getNews());
        return ApiResponse.success("result", newsService.getNews());
    }

    @GetMapping("/detail/{newsId}")
    /*
        detail.html에서 해당 뉴스의 id 값을 통해 Get요청을 보내는데, 해당 id의 뉴스 정보를 리턴해주는 역할을 한다.
        param: newsId // 해당 뉴스의 id
        return: 뉴스정보가 담긴 ResponseNewsDetailDTO
     */
    public ApiResponse<ResponseNewsDetailDTO> getNewsDetial(@PathVariable String newsId){
        return ApiResponse.success("result", newsService.getNewsDetail(newsId));
    }

//    @PostMapping("/log")
//    public ApiResponse<String> createNewsAccessLog(@RequestBody RequestCreateNewsAccessLogDTO requestCreateNewsAccessLogDTO){
//        /*
//            사용자가 뉴스 preview를 클릭해 detail 페이지에 접근할 때마다 기록 작성
//            param: form('news_id', 'user_id', 'news_title')을 RequestCreateNewsAccessLogDTO에 담아 전달
//            return: 로그를 작성한 뉴스의 id
//         */
//        System.out.println("test");
//        String newsId = newsService.setNewsAccesLog(requestCreateNewsAccessLogDTO);
//        return ApiResponse.success("result", newsId);
//    }
}
