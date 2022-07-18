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

    /**
     * detail.html에서 해당 뉴스의 id와 읽고 있는 userId를 가지고 bookmarkLog를 생성
     * @param requestBookmarkDTO
     * @return 생성된 북마크의 식별번호(Id: String, based on UUID4)
     */
    @PostMapping("")
    public ApiResponse<String> createBookmark(@RequestBody RequestBookmarkDTO requestBookmarkDTO){
        return ApiResponse.success("result", bookmarkService.createBookmark(requestBookmarkDTO));
    }

    /**
     * detail.html에서 해당 뉴스의 id와 읽고 있는 userId를 가지고 사용자가 뉴스를 북마크했는지 판별
     * @param requestBookmarkDTO
     * @return 해당 뉴스에 대한 사용자의 북마크 여부 (True||False)
     */
    @GetMapping("")
    public ApiResponse<Boolean> isBookmark(@RequestBody RequestBookmarkDTO requestBookmarkDTO){
        return ApiResponse.success("result", bookmarkService.isBookmarkCheck(requestBookmarkDTO));
    }


    /**
     * detail.html에서 해당 뉴스의 id와 읽고 있는 userId를 가지고 사용자가 뉴스를 북마크했는지 판별
     * @param requestBookmarkDTO
     * @return 해당 뉴스에 대한 사용자의 북마크 여부 (True||False)
     */
    @DeleteMapping("")
    public ApiResponse<String> cancel(@RequestBody RequestBookmarkDTO requestBookmarkDTO){
        return ApiResponse.success("result", bookmarkService.deleteBookmark(requestBookmarkDTO));
    }

    /**
     * profile.html에서 보내준 profile userId의 bookmark 일괄 조회
     * @param userId
     * @return 해당 유저가 북마킹한 내역인 List<Bookmark>를 리턴
     */
    @GetMapping("/profiles/{userId}")
    public ApiResponse<ResponseBookmarkDTO> bookmark(@PathVariable String userId){
        return ApiResponse.success("result", bookmarkService.selectAllUserBookmark(userId));
    }
}
