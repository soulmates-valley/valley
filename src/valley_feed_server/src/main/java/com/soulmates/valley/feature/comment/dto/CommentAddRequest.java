package com.soulmates.valley.feature.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class CommentAddRequest {

    @NotNull(message = "post id는 필수 입력 값입니다.")
    private Long postId;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Size(max = 300, message = "댓글의 최대 길이는 300입니다.")
    private String content;
}
