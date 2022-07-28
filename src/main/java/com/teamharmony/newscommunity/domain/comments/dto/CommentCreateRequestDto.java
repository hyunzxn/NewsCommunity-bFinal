package com.teamharmony.newscommunity.domain.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommentCreateRequestDto {
    @Size(max = 300, message = "글자수 초과")
    private String content;

    private String newsId;
}
