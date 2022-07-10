package com.teamharmony.newscommunity.bookmarks.service;

import com.teamharmony.newscommunity.bookmarks.dto.RequestBookmarkDTO;
import com.teamharmony.newscommunity.bookmarks.dto.ResponseBookmarkDTO;
import com.teamharmony.newscommunity.bookmarks.entity.Bookmarks;
import com.teamharmony.newscommunity.bookmarks.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    //1. TODO: 북마크 생성 (req: newsId, userId) use by deatil.html (res: ApiResponse.success("result", newsID))   //DONE
    public String createBookmark(RequestBookmarkDTO requestBookmarkDTO){
        /*  /api/bookmarks의 경로로 요청된 POST 메서드의 응답을 처리하는 메서드, 북마크 로그를 생성
            @param: newsId, userId가 담겨있는 requestBookmarkDTO가 담겨있음
            @return: 저장된 북마크 로그의 id가 리턴
         */
        // + TODO: 북마크 생성 예외처리(기존에 news, user id가 같은 로그가 존재시 예외처리)
        Bookmarks bookmark = Bookmarks.builder()
                .requestBookmarkDTO(requestBookmarkDTO)
                .build();
        return bookmarkRepository.save(bookmark).getId();
    }

    //2. TODO: 북마크 여부 검증(req: newsId, userId) use by detail.html (res: ApiResponse.success("result", newsId))      //DONE
    public Boolean isBookmarkCheck(RequestBookmarkDTO requestBookmarkDTO){
        /*  /api/bookmarks의 경로로 요청된 GET 메서드의 응답을 처리하는 메서드, 북마크 여부 검증을 함
            @param: newsId, userId가 담겨있는 requestBookmarkDTO가 담겨있음
            @return: 해당 newsId, userId의 복합키가 북마크 db에 존재하는지 여부 리턴(true||false)
         */
        try {
            bookmarkRepository.findByNewsIdAndUserId(requestBookmarkDTO.getNewsId(), requestBookmarkDTO.getUserId()).getUserId();
            return true;
        }catch(NullPointerException n){
            System.out.println(n.toString());
            return false;
        }
    }

    //3. TODO: 북마크 취소 (req: newId, userId) use by detail.html     (res: ApiResponse.success("result", newsId))
    @Transactional
    public String deleteBookmark(RequestBookmarkDTO requestBookmarkDTO){
        /*  /api/bookmarks의 경로로 요청된 DELETE 메서드의 응답을 처리하는 메서드. 북마크 취소의 기능으로, db에 있는 해당 조건의 기록을 삭제
            @param: newsId, userId가 담겨있는 requestBookmarkDTO가 담겨있음
            @return: 해당 newsId, userId의 복합키가 북마크 db에 존재하는지 여부 리턴(true||false)
         */
        // + TODO: 북마크가 존재하지 않으면 예외처리
        bookmarkRepository.deleteBookmarkByNewsIdAndUserId(requestBookmarkDTO.getNewsId(), requestBookmarkDTO.getUserId());
        return requestBookmarkDTO.getNewsId();
    }

    //4. TODO: 북마크 리스트 조회 (req: userId) use by profile.html     (res: ApiResponse.success("result", List<Bookmark>))
    public ResponseBookmarkDTO selectAllUserBookmark(String userId) {
        /* /api/bookmarks/profiles/{userId}의 경로로 요청된 GET 메서드의 응답을 처리하는 메서드, 해당 userId가 포함된 bookmark리스트 리턴
            @param: 클라이언트에서 보낸준 userId
            @return: 해당 userId가 포함된 bookmark 리스트
        */
        return ResponseBookmarkDTO.builder()
                .bookmarksList(bookmarkRepository.findAllByUserId(userId))
                .build();
    }
}