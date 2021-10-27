package com.soulmates.valley.domain.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@Node("Post")
public class Post {

    @Id
    private Long postId;

    private LocalDateTime createDt;
}
