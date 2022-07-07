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

    @PostMapping("/user/comments")
    public Long saveComment(@RequestBody CommentRequestDto commentRequestDto,
                            @AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        return commentService.createComment(commentRequestDto, username);
    }

    @GetMapping("/user/comments/{news_id}")
    public List<Comment> getComment(@PathVariable String news_id) {
        return commentService.findComments(news_id);
    }



    @PutMapping("/user/comments/{id}")
    public Long editComment(@PathVariable Long id,
                            @RequestBody CommentRequestDto commentRequestDto) {
        commentService.updateComment(id, commentRequestDto);
        return id;
    }

    @DeleteMapping("/user/comments/{id}")
    public Long deleteComment(@PathVariable Long id) {
        commentRepository.deleteById(id);
        return id;
    }

    @GetMapping("/user/comments/count/{news_id}")
    public int getCommentCount(@PathVariable String news_id) {
        return commentService.findComments(news_id).size();
    }

    @GetMapping("/user/comments/sort/{news_id}")
    public List<Comment> sortComments(@PathVariable String news_id, @RequestParam String direction) {
        if (direction.equals("DESC")) {
            return commentService.getSortedCommentsDesc(news_id);
        } else {
            return commentService.getSortedCommentsAsc(news_id);
        }
    }
}
