# 할머니는 다 들어주셔

###### <br>

## 1. 프로젝트 개요

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

![ERD](https://drive.google.com/uc?export=view&id=1tgbvSpsGVIznFvMTlj4kAaJ5kARCtM45)



<br>

## 4. 프로젝트 아키텍처

![Architecture](https://drive.google.com/uc?export=view&id=1nB18a9fwVLoBpLk405UrrkrLDhwMnml0)



<br>

## 5. 기여한 부분

**담당 기능: 댓글 관련 기능**

- 뉴스 상세페이지 댓글 CRUD
- 프로필 페이지 유저 댓글 모아보기
- 뉴스 상세페이지 댓글 좋아요 기능
- 뉴스 상세페이지 댓글 시간순 정렬 기능
- 뉴스 상세페이지 및 프로필 페이지 댓글 페이징 기능


[📌 패키지 확인](https://github.com/hyunzxn/NewsCommunity-bFinal/tree/develop/src/main/java/com/teamharmony/newscommunity/domain/comments) (클릭하시면 이동합니다.)



## 6. 기억에 남는 기능

> **6-1 좋아요 기능**
>
> - 선정이유
>
>   - 복붙과 다른 사람의 도움을 받기보다 제 힘으로 완성한 비중이 더 큰 첫 코드입니다.
>   - 이 기능을 완성한 이후 '내 힘으로도 코드를 짤 수 있다'는 자신감이 생겼습니다.
>
>   <br>
>
>   <details>
>     <summary>좋아요 기능 흐름</summary>
>       <img src="https://drive.google.com/uc?export=view&id=1BZHn_m0McWTbx8fuW2kzX98CtE7ZddYB"/>
>   </details>
> [📌 코드확인](https://github.com/hyunzxn/NewsCommunity-bFinal/blob/ad132d8b888f46084cd7f8cb3d8f70a3c119e8ac/src/main/java/com/teamharmony/newscommunity/domain/comments/service/LikesService.java#L27-L49) (클릭하시면 이동합니다.)
>

<br>

> **6-2 페이징 기능**
>
> - 선정이유
>   - 뉴스 상세페이지 댓글 로딩 시간이 길다는 것에 대한 성능적 보완을 위해 도입한 기능이라 기억에 남습니다.
>   - 고객 피드백에서 많은 지적을 받은 기능이라 기억에 남습니다.
>   
>   <br>
>   
>   <details>
>     <summary>페이징 기능 흐름</summary>
>     <img src="https://drive.google.com/uc?export=view&id=1Q0RKZuXiD4B68tKUGtkex4zlcRXbFijm" />
>   </details>
>   [📌 컨트롤러 코드확인](https://github.com/hyunzxn/NewsCommunity-bFinal/blob/7ce08f0d2468e385e657e61f1ec9970c329c848c/src/main/java/com/teamharmony/newscommunity/domain/comments/controller/CommentController.java#L60-L68) (클릭하시면 이동합니다.) <br>
>   [📌 서비스 코드확인](https://github.com/hyunzxn/NewsCommunity-bFinal/blob/7ce08f0d2468e385e657e61f1ec9970c329c848c/src/main/java/com/teamharmony/newscommunity/domain/comments/service/CommentService.java#L56-L74) (클릭하시면 이동합니다.)
>   
>   

<br>

## 7. 핵심 트러블 슈팅

### 페이징 기능 

- 고객 피드백에서 많은 지적을 받은 기능<br>
![고객피드백-댓글 응답 결과](https://drive.google.com/uc?export=view&id=1TqfnAkuNaZSRDLBEErmcYLAxhw3ZvqUE)<br>
<br>

- 문제 상황 : 댓글의 총 개수와 상관없이 페이지 locator 번호가 나타나고 해당 페이지 번호를 눌렀을 때 아무 댓글이 없는 페이지로 이동됨.<br>
<br>

- 문제 원인
  - Spring Data JPA을 이용해서 페이징 기능 구현 시 Pageable 인터페이스와 Page 인터페이스를 사용함.
  - 이 때 기존 코드는 반환하는 타입을 `List<CommentReponseDto>` 으로 작성함.
  - `Page<CommentResponseDto>`타입으로 해주지 않음으로써 프론트엔드에 전달하는 응답에서 **totalElements**가 null이 됨.
  - 이로 인해 댓글의 총 개수와 상관없이 프론트엔드에서 지정해놓은 총 페이지 개수만큼 페이지 번호가 보이게 되고 페이지 번호를 눌렀을 때도 해당 페이지로 이동이 되는 현상이 발생함.<br>
  <br>

- 문제 해결
  - Service 와 Controller에서 댓글을 조회하는 메소드와 API의 Return 타입을 `Page<CommentResponseDto>`타입으로 수정



[📌 코드 변경 커밋 확인](https://github.com/hyunzxn/NewsCommunity-bFinal/commit/2bf32ded6f960d18064f65388695be8bf4e3bf42) (클릭하시면 이동합니다.)

<br>

## 8. 기타 트러블 슈팅

> **git push 안 되던 이슈**
>
> - 프로젝트를 진행하다가 갑자기 git push가 되지 않는 이슈 발생  [📌 이슈 해결 확인](https://moon-daniel.tistory.com/entry/220722-Today-I-Learned?category=1004799) (클릭하시면 이동합니다.)

<br>
