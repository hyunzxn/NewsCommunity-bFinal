package com.teamharmony.newscommunity.comments.repository;

import com.teamharmony.newscommunity.comments.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByNewsId(String newsId, Pageable pageable);
    List<Comment> findByNewsIdOrderByCreatedAtDesc(String newsId);
    List<Comment> findByNewsIdOrderByCreatedAtAsc(String newsId);
    List<Comment> findByUser_Id(Long userId);
    Integer countByNewsId(String newsId);
}
