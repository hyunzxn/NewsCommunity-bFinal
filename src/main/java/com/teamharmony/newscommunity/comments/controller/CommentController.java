package com.teamharmony.newscommunity.comments.controller;

import com.teamharmony.newscommunity.comments.dto.CommentRequestDto;
import com.teamharmony.newscommunity.comments.entity.Comment;
import com.teamharmony.newscommunity.comments.repository.CommentRepository;
import com.teamharmony.newscommunity.comments.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;

    /**
     * requestBody로 넘어오는 댓글내용과 newsId를 받아서 저장합니다.
     * @param commentRequestDto
     * @param user
     * @return 저장된 댓글의 id
     */
    @PostMapping("/user/comments")
    public Long saveComment(@RequestBody CommentRequestDto commentRequestDto,
                            @AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        return commentService.createComment(commentRequestDto, username);
    }

    /**
     * 해당 뉴스에 해당하는 댓글 목록을 보여줍니다.
     * @param news_id
     * @return 해당 뉴스 아이디에 해당하는 댓글 리스트
     */
    @GetMapping("/user/comments/{news_id}")
    public List<Comment> getComment(@PathVariable String news_id) {
        return commentService.findComments(news_id);
    }


    /**
     * 특정 댓글을 수정합니다.
     * @param id
     * @param commentRequestDto
     * @return 수정된 댓글의 id
     */
    @PutMapping("/user/comments/{id}")
    public Long editComment(@PathVariable Long id,
                            @RequestBody CommentRequestDto commentRequestDto) {
        commentService.updateComment(id, commentRequestDto);
        return id;
    }

    /**
     * 특정 댓글을 삭제합니다.
     * @param id
     * @return 삭제된 댓글의 id
     */
    @DeleteMapping("/user/comments/{id}")
    public Long deleteComment(@PathVariable Long id) {
        commentRepository.deleteById(id);
        return id;
    }

    /**
     * 댓글의 좋아요 개수를 반환합니다.
     * @param news_id
     * @return 특정 댓글의 좋아요 개수
     */
    @GetMapping("/user/comments/count/{news_id}")
    public int getCommentCount(@PathVariable String news_id) {
        return commentService.findComments(news_id).size();
    }

    /**
     * 파라미터로 받은 direction에 따라 댓글 리스트를 정렬한 뒤 반환합니다.
     * @param news_id
     * @param direction
     * @return 재정렬된 댓글 리스트
     */
    @GetMapping("/user/comments/sort/{news_id}")
    public List<Comment> sortComments(@PathVariable String news_id, @RequestParam String direction) {
        if (direction.equals("DESC")) {
            return commentService.getSortedCommentsDesc(news_id);
        } else {
            return commentService.getSortedCommentsAsc(news_id);
        }
    }
}
