package com.soulmates.valleyfollowserver.domain.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Builder
@EqualsAndHashCode(of = "userId")
@Node("User")
public class UserNode {
    @Id
    private Long userId;

    private String nickname;

    private String profileImg;

    private String description;

    private LocalDateTime lastPostDt;
}
