package com.soulmates.valley.domain.model.graph;

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
public class PostNode {

    @Id
    private Long postId;

    private LocalDateTime createDt;
}
