package com.soulmates.valley.feature.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Builder
@Setter
@AllArgsConstructor
@Getter
public class CommentPageLimitReuqest {

    @NotNull(message = "page는 필수 입력값 입니다.")
    private int page;

    @NotNull(message = "size는 필수 입력값 입니다.")
    @Max(value = 100, message = "요청할 수 있는 size의 최대크기는 100입니다.")
    private int size;
}
