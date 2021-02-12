package com.soulmates.valleyfollowserver.feature.follow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class FollowInfo {
    private Long userId;

    private String nickname;

    private String profileImg;

    private String description;

    @JsonProperty("isFollowed")
    private boolean isFollowed;
}
