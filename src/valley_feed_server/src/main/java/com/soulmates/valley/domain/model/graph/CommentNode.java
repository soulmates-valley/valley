package com.soulmates.valley.domain.model.graph;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@Node("Comment")
public class CommentNode {
    @Id
    @GeneratedValue
    private Long id;

    private String content;

    private LocalDateTime createDt;
}
