package com.teamharmony.newscommunity.comments.controller;

import com.teamharmony.newscommunity.comments.entity.Likes;
import com.teamharmony.newscommunity.comments.service.LikesService;
import com.teamharmony.newscommunity.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LikesController {

    private final LikesService likesService;
    private final UserRepository userRepository;

    /**
     * 좋아요 요청을 받는 API
     * @param id
     * @param user
     */
    @PostMapping("/user/likes/{id}")
    public ResponseEntity<?> likes(@PathVariable Long id,
                                   @AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        likesService.likes(id, username);
        return ResponseEntity.ok().body("좋아요");
    }

    /**
     * 특정 댓글에 표시된 좋아요 개수를 보여주는 API
     * @param commentId
     * @return
     */
    @GetMapping("/user/likes/count/{commentId}")
    public int getLikedCommentsCountByCommentId(@PathVariable Long commentId) {
        return likesService.LikedCommentsCount(commentId);
    }
}
