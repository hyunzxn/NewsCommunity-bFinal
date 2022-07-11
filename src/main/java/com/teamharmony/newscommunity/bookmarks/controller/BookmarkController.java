package com.teamharmony.newscommunity.bookmarks.controller;

import com.teamharmony.newscommunity.bookmarks.dto.RequestBookmarkDTO;
import com.teamharmony.newscommunity.bookmarks.dto.ResponseBookmarkDTO;
import com.teamharmony.newscommunity.bookmarks.service.BookmarkService;
import com.teamharmony.newscommunity.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    //1. TODO: 북마크 생성 (req: newsId, userId) use by deatil.html    (res: ApiResponse.success("result", newsID))   //DONE
    @PostMapping("")
    public ApiResponse<String> createBookmark(@RequestBody RequestBookmarkDTO requestBookmarkDTO){
        /* detail.html에서 해당 뉴스의 id와 읽고 있는 userId를 가지고 bookmarkLog를 생성
            @param: 북마크 생성을 위한 RequestBookmarkDTO (newsId, userId)
            @return: 생성된 북마크의 식별번호(Id: String, based on UUID4)
         */
        return ApiResponse.success("result", bookmarkService.createBookmark(requestBookmarkDTO));
    }

    //2. TODO: 북마크 여부 검증(req: newsId, userId) use by detail.html (res: ApiResponse.success("result", newsId))
    @GetMapping("")
    public ApiResponse<Boolean> isBookmark(@RequestBody RequestBookmarkDTO requestBookmarkDTO){
        /* detail.html에서 해당 뉴스의 id와 읽고 있는 userId를 가지고 사용자가 뉴스를 북마크했는지 판별
            @param: 북마크 여부 판별을 위한 RequestBookmarkDTO (newsId, userId)
            @return: 해당 뉴스에 대한 사용자의 북마크 여부 (True||False)
         */
        return ApiResponse.success("result", bookmarkService.isBookmarkCheck(requestBookmarkDTO));
    }

    //3. TODO: 북마크 취소 (req: newId, userId) use by detail.html     (res: ApiResponse.success("result", newsId))
    @DeleteMapping("")
    public ApiResponse<String> cancel(@RequestBody RequestBookmarkDTO requestBookmarkDTO){
        /* detail.html에서 해당 뉴스의 id와 읽고 있는 userId를 가지고 사용자가 뉴스를 북마크했는지 판별
            @param: 북마크 여부 판별을 위한 RequestBookmarkDTO (newsId, userId)
            @return: 해당 뉴스에 대한 사용자의 북마크 여부 (True||False)
         */
        return ApiResponse.success("result", bookmarkService.deleteBookmark(requestBookmarkDTO));
    }

    //4. TODO: 북마크 리스트 조회 (req: userId) use by profile.html     (res: ApiResponse.success("result", List<Bookmark>))
    @GetMapping("/profiles/{userId}")
    public ApiResponse<ResponseBookmarkDTO> bookmark(@PathVariable String userId){
        /* profile.html에서 보내준 profile userId의 bookmark 일괄 조회
            @param: 북마크 검색시 필요한 userId
            @return: 해당 유저가 북마킹한 내역인 List<Bookmark>를 리턴
         */
        return ApiResponse.success("result", bookmarkService.selectAllUserBookmark(userId));
    }
}
