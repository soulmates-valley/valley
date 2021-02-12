package com.soulmates.valleyfollowserver.common.event.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FollowCreateEvent {
    private Long fromUserId;
    private Long toUserId;
}
