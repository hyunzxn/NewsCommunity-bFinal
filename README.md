# 할머니는 다 들어주셔

###### <br>

### 1. 프로젝트 개요

- 개발기간: 2022.06.24 ~ 2022.07.29 
- 참여인원: 4명
- 소개: 네이버 스포츠 뉴스를 스크래핑 해와서 그것에 대한 댓글을 남기며 의견을 공유하는 커뮤니티 서비스



<br>

## 2. 사용기술

- Backend: Java 11 / Spring Boot 2.7.0 / Gradle 7.4.1 / Spring Data JPA
- Database: AWS RDS (MySQL)
- Security: Spring Security
- Infra: AWS ElasticBeanstalk
- CI/CD: Github Action



<br>

## 3. ERD 설계

<img src="https://drive.google.com/uc?export=view&id=1tgbvSpsGVIznFvMTlj4kAaJ5kARCtM45" alt="ERD" style="zoom:50%;" /> 



<br>

## 4. 프로젝트 아키텍처

<img src="https://drive.google.com/uc?export=view&id=1nB18a9fwVLoBpLk405UrrkrLDhwMnml0" alt="아키텍쳐" style="zoom:50%;" />



<br>

## 5. 기여한 부분

**담당 기능: 댓글 관련 기능**

- 뉴스 상세페이지 댓글 CRUD
- 프로필 페이지 유저 댓글 모아보기
- 뉴스 상세페이지 댓글 좋아요 기능
- 뉴스 상세페이지 댓글 시간순 정렬 기능
- 뉴스 상세페이지 및 프로필 페이지 댓글 페이징 기능

<br>

[📌 코드 확인](https://github.com/hyunzxn/NewsCommunity-bFinal/tree/develop/src/main/java/com/teamharmony/newscommunity/domain/comments) (클릭하시면 이동합니다.)



## 6. 기억에 남는 기능

> **6-1 좋아요 기능**
>
> - 선정이유: 이 기능은 처음으로 복붙과 다른 사람의 도움보다 제 힘으로 완성한 비중이 더 큰 기능입니다. 이 기능을 완성한 이후에 '나도 내 힘으로 코드를 짤 수 있구나' 하는 자신감이 생겼기 때문에 기억에 남는 첫번째 기능으로 선정했습니다.
>
>   <br>
>
>   <details>
>     <summary>좋아요 기능 흐름</summary>
>       <img src="https://drive.google.com/uc?export=view&id=1RtGESL8Rh53VKN2uLTSPjCLaXK4Um5-9"/>
>
>   </details>
> [📌 코드확인](https://github.com/hyunzxn/NewsCommunity-bFinal/blob/ad132d8b888f46084cd7f8cb3d8f70a3c119e8ac/src/main/java/com/teamharmony/newscommunity/domain/comments/service/LikesService.java#L27-L49) (클릭하시면 이동합니다.)
>   





