package com.teamharmony.newscommunity.news.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamharmony.newscommunity.common.ApiResponse;
import com.teamharmony.newscommunity.news.dto.ResponseNewsDetailDTO;
import com.teamharmony.newscommunity.news.entity.NewsTable;
import com.teamharmony.newscommunity.news.service.NewsService;
import com.teamharmony.newscommunity.users.repository.TokensRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

//1.  퓨어 자바, 테스트 먼저 보기 => 어떻게? => junit 거의 쓰긴 하지만, 프레임 워크보다 테스트에 대한 것부터 이해
// ex) 단위, 기능 인수 테스트 등 --, 테스트 왜 해야 되는지에 대해 이해
// 책, 강의 추천: 백기선(장기적으로, 시간 투자): https://www.inflearn.com/course/the-java-application-test#curriculum,

//2.  스프링의 구조? => 목 객체 이해를 위해 필요  => 어떻게 공부해? => 스프링 컨테이너, 빈, 의존성주입,
//3.  JPA는 김영한니 ㅁ책

// 1. 테스트 코드 디버깅??
// 2. NewsMockConfig 나누는법?
// 3.인코딩이 이상하게 돼
// .addFilters(new CharacterEncodingFilter("UTF-8", true))


@EnableMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@RunWith(SpringRunner.class)      // SpringRunner라는 스프링 실행자를 사용
@WebMvcTest(NewsController.class) // 내가 테스트할 범위를 NewsScontroller로 지정
class NewsControllerTest {
    @Autowired                   // 스프링이 관리하는 MockMvc빈 주입을 위해 선언
    private MockMvc mockMvc;     // 스프링 Mvc의 시작점, 웹 API 테스트시 사용. 얘 덕분에 HTTP GET,POST 등에 대한 API 테스트 가능
    @MockBean
    UserDetailsService userDetailsService;
    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @MockBean
    TokensRepository tokensRepository;

    @MockBean
    NewsService newsService;
    @MockBean
    ApiResponse apiResponse;


    @Test
    @DisplayName("getNewsDetailTest() Connect")
    public void getNewsDetailTest() throws Exception{ // 제목 수정 DONE
        // http://localhost:4993/api/news/detail/2786fee6-d69a-4017-8e66-f5f8bd98f54a
        // Given

        NewsTable newsTable = new NewsTable(
                "2786fee6-d69a-4017-8e66-f5f8bd98f54a",
                "이름이 ‘손날두’고, 브라질인이었다면… SON 제외 논란 ‘풍자’",
                "스포탈코리아] 김희웅 기자= 손흥민(토트넘 홋스퍼)을 외면한 잉글랜드프로축구선수협회(PFA)를 향한 비판은 여전합니다.  토트넘의 유럽축구연맹(UEFA) 챔피언스리그 진출도 손흥민의 공이 가장 컸습니다.  리그 베스트11 선정은 떼 놓은 당상이었습니다.  그런데 공동 득점왕을 차지한 모하메드 살라(리버풀)는 포함됐으나 손흥민의 이름이 빠지면서 논란이 불거졌습니다.  PFA 올해의 팀에는 손흥민보다 팀 성적, 개인 스텟이 저조한 크리스티아누 호날두(맨체스터 유나이티드)가 한자리를 꿰찼습니다.",
                "https://imgnews.pstatic.net/image/139/2022/06/24/0002168932_001_20220624060001432.jpg?type=w647",
                "https://sports.news.naver.com//epl/news/read?oid=139&aid=0002168932",
                "[스포탈코리아] 김희웅 기자= 손흥민(토트넘 홋스퍼)을 외면한 잉글랜드프로축구선수협회(PFA)를 향한 비판은 여전하다. 손흥민은 2015년 토트넘 유니폼을 ...",
                "2022.06.24. 오전 06:00",
                0L
        );
        ResponseNewsDetailDTO responseNewsDetailDTO = ResponseNewsDetailDTO.builder() // ResponseNewsDetailDTO를 newsTable 초기화
                .newsTable(newsTable)
                .build();

        // When: when 메소드를 이용해 스터빙, 스터빙할 메소드 삽입 후 어떻게 제어할건지 메서드 체이닝 형태로 작성
        when(userDetailsService.loadUserByUsername(eq("cksgurwkd1"))).thenReturn(new org.springframework.security.core.userdetails.User("cksgurwkd1", "rkawk123", List.of()));
        when(newsService.getNewsDetail(eq("2786fee6-d69a-4017-8e66-f5f8bd98f54a"))).thenReturn(responseNewsDetailDTO);

        // Then
        mockMvc.perform(                              // 해당 경로로 get 요청 보냄.
                MockMvcRequestBuilders.get("http://localhost:4993/api/news/details/2786fee6-d69a-4017-8e66-f5f8bd98f54a")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()                                                                                               //MvcPerform의 결과 검증, HTTP Header의 Status 검증
        ).andExpect(
                MockMvcResultMatchers.content().string(String.valueOf(ParseObjToJSON(ApiResponse.success("result", responseNewsDetailDTO))))  // 응답 본문의 내용 검증
        ).andDo(MockMvcResultHandlers.print());
    }

    public String ParseObjToJSON(ApiResponse<ResponseNewsDetailDTO> detailDTO) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        //Object to JSON in String
        String targetJSON = mapper.writeValueAsString(detailDTO);
        return targetJSON;
    }
}