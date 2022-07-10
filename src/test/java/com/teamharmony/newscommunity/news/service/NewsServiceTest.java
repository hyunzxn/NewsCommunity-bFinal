package com.teamharmony.newscommunity.news.service;

import com.teamharmony.newscommunity.news.dto.requestCreateDTO.RequestCreateNewsAccessLogDTO;
import com.teamharmony.newscommunity.news.entity.NewsAccessLog;
import com.teamharmony.newscommunity.news.repository.NewsAccessLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Nested
    @DisplayName("뉴스 접근 기록 관련 테스트")
    class NewsAccessTest {
        @Mock                                                       // 레포지 토리 직접 접근을 막기 위해, 목 객체로 주입받아 사용
        private NewsAccessLogRepository newsAccessLogRepository;         // newsAccess관련 서비스를 위한 레포지토리

        @InjectMocks                                                // 목을 주입받아 사용할 service 등록
        private NewsService newsService;                                // 뉴스 접근 기록 생성, 조회수 증가 및 뉴스 정보 listing등의 기능을 하는 서비스

        @Test
        @DisplayName("뉴스 접근 기록 생성 테스트")
        void setNewsAccessLog() {
            when(this.newsAccessLogRepository.save(any(NewsAccessLog.class))) //when// // 2. save를 통해 인자값 입력시 첫번째 arg 리턴
                    .then(AdditionalAnswers.returnsFirstArg());

            RequestCreateNewsAccessLogDTO expected =                          //given//   // 1. 예측값 생성, (Test Title라는 문자열을 title로 가지고있는 newsTable 객체)
                    RequestCreateNewsAccessLogDTO.builder()
                            .news_id(UUID.randomUUID().toString())
                            .user_id("테스트용 아이디입니다")
                            .title("테스트용 타이틀입니다.")
                            .build();
            String actual = this.newsService.setNewsAccessLog(expected);      //then//  //3. 생성한 예측값을 Mock으로 주입받은 newsService를 통해 add
            assertEquals(expected.getNews_id(), actual);
        }
    }

    @Test
    void getNews() {
    }

    @Test
    void getNewsDetail() {
    }

    @Test
    void addView() {
    }
}