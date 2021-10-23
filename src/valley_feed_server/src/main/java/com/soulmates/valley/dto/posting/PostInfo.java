package com.soulmates.valley.dto.posting;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@EqualsAndHashCode(of = "postId")
@Getter
public class PostInfo implements Serializable {

    private Long postId;

    private LocalDateTime createDt;

    private Long userId;

    private boolean isUserLiked;
}
