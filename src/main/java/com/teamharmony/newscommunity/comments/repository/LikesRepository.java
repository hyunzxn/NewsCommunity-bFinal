package com.teamharmony.newscommunity.comments.repository;

import com.teamharmony.newscommunity.comments.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    // 외래키로 있는 comment_id로 찾는 쿼리메소드 입니다.
    List<Likes> findByComment_CommentId(Long commentId);

    // 외래키로 있는 comment_id 와 user_id로 유저가 특정 댓글에 좋아요를 한 이력을 조회하는 쿼리메소드입니다.
    Likes findByComment_CommentIdAndUser_Id(Long commentId, Long userId);

    // 외래키로 있는 comment_id 와 user_id로 좋아요 테이블에서 좋아요 기록을 삭제하는 쿼리메소드입니다.
    void deleteByComment_CommentIdAndUser_id(Long commentId, Long userId);
}
