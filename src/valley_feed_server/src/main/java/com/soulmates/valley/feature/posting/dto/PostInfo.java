package com.soulmates.valley.feature.posting.dto;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;

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
