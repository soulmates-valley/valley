package com.soulmates.valleyfollowserver.feature.follow.dto;

import lombok.*;

@Builder
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class FollowCount {
    private int inCnt;

    private int outCnt;
}
