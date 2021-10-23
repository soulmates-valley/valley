package com.soulmates.valley.dto.feed;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Getter
public class FeedLimitRequest {

    @NotNull(message = "page는 필수 입력값 입니다.")
    private long page;

    @NotNull(message = "size는 필수 입력값 입니다.")
    @Max(value = 20, message = "요청할 수 있는 size의 최대크기는 20입니다.")
    private long size;
}
