package com.teamharmony.newscommunity.comments.service;

import com.teamharmony.newscommunity.comments.dto.CommentCreateRequestDto;
import com.teamharmony.newscommunity.comments.dto.CommentEditRequestDto;
import com.teamharmony.newscommunity.comments.dto.CommentResponseDto;
import com.teamharmony.newscommunity.comments.entity.Comment;
import com.teamharmony.newscommunity.comments.repository.CommentRepository;
import com.teamharmony.newscommunity.users.dto.response.ProfileResponseDto;
import com.teamharmony.newscommunity.users.dto.response.UserResponseDto;
import com.teamharmony.newscommunity.users.entity.User;
import com.teamharmony.newscommunity.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createComment(CommentCreateRequestDto commentCreateRequestDto, String username) {
        Comment comment = new Comment(commentCreateRequestDto);
        User user = userRepository.findByUsername(username);
        user.addComment(comment);
    }

    /* 기존에 Entity를 바로 리턴해주던 방식의 코드
    public List<Comment> findComments(String newsId) {
        return commentRepository.findAllByNewsId(newsId);
    }
    */

    /**
     * DB에서 newsId로 데이터를 모두 조회한 다음, 필요한 값들만 CommentResponseDto에 담는 함수
     * @param newsId
     * @return
     */
    public List<CommentResponseDto> findComments(String newsId) {
        List<Comment> commentList = commentRepository.findAllByNewsId(newsId);
        return commentList.stream().map(comment -> CommentResponseDto.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .modifiedAt(comment.getModifiedAt())
                        .createdAt(comment.getCreatedAt())
                        .profileResponseDto(new ProfileResponseDto(comment.getUser().getProfile()))
                        .build())
                        .collect(Collectors.toList());
    }

    @Transactional
    public void updateComment(Long id, CommentEditRequestDto commentEditRequestDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("댓글을 찾을 수 없습니다")
        );
        comment.update(commentEditRequestDto);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    /* 기존 Entity 바로 return 하던 코드
    public List<Comment> getSortedCommentsDesc(String newsId) {
        return commentRepository.findByNewsIdOrderByCreatedAtDesc(newsId);
    }
     */


    public List<CommentResponseDto> getSortedCommentsDesc(String newsId) {
        List<Comment> commentList = commentRepository.findByNewsIdOrderByCreatedAtDesc(newsId);
        return commentList.stream().map(comment -> CommentResponseDto.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .modifiedAt(comment.getModifiedAt())
                        .createdAt(comment.getCreatedAt())
                        .profileResponseDto(new ProfileResponseDto(comment.getUser().getProfile()))
                        .build())
                .collect(Collectors.toList());
    }

    /*
    public List<Comment> getSortedCommentsAsc(String newsId) {
        return commentRepository.findByNewsIdOrderByCreatedAtAsc(newsId);
    }
     */
    public List<CommentResponseDto> getSortedCommentsAsc(String newsId) {
        List<Comment> commentList = commentRepository.findByNewsIdOrderByCreatedAtAsc(newsId);
        return commentList.stream().map(comment -> CommentResponseDto.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .modifiedAt(comment.getModifiedAt())
                        .createdAt(comment.getCreatedAt())
                        .profileResponseDto(new ProfileResponseDto(comment.getUser().getProfile()))
                        .build())
                .collect(Collectors.toList());
    }

    public List<CommentResponseDto> getCommentsByUserId(String username) {
        User user = userRepository.findByUsername(username);
        Long userId = user.getId();

        List<Comment> commentList = commentRepository.findByUser_Id(userId);
        return commentList.stream().map(comment -> CommentResponseDto.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .modifiedAt(comment.getModifiedAt())
                        .createdAt(comment.getCreatedAt())
                        .profileResponseDto(new ProfileResponseDto(comment.getUser().getProfile()))
                        .build())
                .collect(Collectors.toList());
    }
}
