package com.teamharmony.newscommunity.domain.comments.controller;

import com.teamharmony.newscommunity.domain.comments.dto.CommentCreateRequestDto;
import com.teamharmony.newscommunity.domain.comments.dto.CommentEditRequestDto;
import com.teamharmony.newscommunity.domain.comments.dto.CommentResponseDto;
import com.teamharmony.newscommunity.domain.comments.service.CommentService;
import com.teamharmony.newscommunity.common.annotation.CurrentUser;
import com.teamharmony.newscommunity.exception.ErrorResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    /**
     * requestBody로 넘어오는 댓글내용과 newsId를 받아서 저장합니다.
     * @param commentCreateRequestDto
     * @param username
     * @return 저장된 댓글의 id
     */
    @ApiOperation(value = "댓글 저장")
    @PostMapping("/user/comments")
    public ResponseEntity<?> saveComment(@RequestBody @Valid CommentCreateRequestDto commentCreateRequestDto,
                                         @CurrentUser String username) {
        commentService.createComment(commentCreateRequestDto, username);
        return ResponseEntity.ok().body("댓글 작성 성공");
    }

    /**
     * 해당 뉴스에 해당하는 댓글 목록을 보여줍니다.
     * @param news_id
     * @return 해당 뉴스 아이디에 해당하는 댓글 리스트
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name="news_id",value = "뉴스 아이디", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name="currentUser",value = "로그인 한 유저",  required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name="isAsc",value = "오름차순 정렬",  required = true, dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name="page",value = "페이지 번호",  required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name="size",value = "페이지에 보일 댓글의 수",  required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name="sortBy",value = "정렬 기준",  required = true, dataType = "string", paramType = "query")
    })
    @ApiOperation(value = "댓글 조회")
    @GetMapping("/comments/{news_id}/{currentUser}")
    public ResponseEntity<Page<CommentResponseDto>> getComment(@PathVariable String news_id,
                                                               @RequestParam("page") int page,
                                                               @RequestParam("size") int size,
                                                               @RequestParam("sortBy") String sortBy,
                                                               @RequestParam("isAsc") boolean isAsc,
                                                               @PathVariable String currentUser) {
        return ResponseEntity.ok().body(commentService.findComments(news_id, (page - 1), size, sortBy, isAsc, currentUser));
    }


    /**
     * 특정 댓글을 수정합니다.
     * @param id
     * @param commentEditRequestDto
     * @return 수정된 댓글의 id
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "댓글의 아이디", required = true, dataType = "int", paramType = "path")
    })
    @ApiOperation(value = "댓글 수정")
    @PutMapping("/user/comments/{id}")
    public ResponseEntity<?> editComment(@PathVariable Long id,
                                         @RequestBody @Valid CommentEditRequestDto commentEditRequestDto) {
        commentService.updateComment(id, commentEditRequestDto);
        return ResponseEntity.ok().body("수정 성공");
    }

    /**
     * 특정 댓글을 삭제합니다.
     * @param id
     * @return 삭제된 댓글의 id
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "댓글의 아이디", required = true, dataType = "int", paramType = "path")
    })
    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/user/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().body("삭제 성공");
    }

    /**
     * 해당 뉴스에 달린 댓글의 개수를 반환합니다.
     * @param news_id
     * @return 해당 기사에 달린 댓글의 개수
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "news_id", value = "뉴스 아이디", required = true, dataType = "string", paramType = "path")
    })
    @ApiOperation(value = "댓글 개수 조회")
    @GetMapping("/user/comments/count/{news_id}")
    public ResponseEntity<Integer> getCommentCount(@PathVariable String news_id) {
        return ResponseEntity.ok().body(commentService.getCommentCount(news_id));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentUser", value = "현재 로그인 한 유저", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "page", value = "페이지 번호", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "한 페이지에 보일 댓글의 개수", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "유저의 아이디", required = true, dataType = "string", paramType = "path")

    })
    @ApiOperation(value = "프로필 페이지에서 유저가 작성한 댓글 조회")
    @GetMapping("/comments/profile/{username}/{currentUser}")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsOnProfilePage(@PathVariable String username,
                                                                             @RequestParam int page,
                                                                             @RequestParam int size,
                                                                             @PathVariable String currentUser) {
        return ResponseEntity.ok().body(commentService.getCommentsByUserId(username, (page-1), size, currentUser));
    }
}
