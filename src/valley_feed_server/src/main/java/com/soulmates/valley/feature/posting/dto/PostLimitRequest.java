package com.soulmates.valley.feature.posting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PostLimitRequest {

    @NotNull(message = "size는 필수 입력값 입니다.")
    @Max(value = 50, message = "요청할 수 있는 size의 최대크기는 50입니다.")
    private int size;

    Long searchUserId;

    //가장 마지막으로 읽은 post Id (선택)
    Long maxPostId = -1L;
}
