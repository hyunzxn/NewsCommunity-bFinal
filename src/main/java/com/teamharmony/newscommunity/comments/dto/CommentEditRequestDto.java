package com.teamharmony.newscommunity.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommentEditRequestDto {
    @Size(max = 300, message = "글자수 초과")
    private String content;
}
