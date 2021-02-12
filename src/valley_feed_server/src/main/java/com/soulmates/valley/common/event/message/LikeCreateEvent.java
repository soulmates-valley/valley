package com.soulmates.valley.common.event.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LikeCreateEvent {
    private Long fromUserId;
    private Long toUserId;
    private Long postId;
}
