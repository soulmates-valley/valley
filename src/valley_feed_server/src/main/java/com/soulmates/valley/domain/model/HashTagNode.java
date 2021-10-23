package com.soulmates.valley.domain.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@Node("HashTag")
public class HashTagNode {

    @Id
    private String content;

    @Relationship(type = "TAG", direction = Relationship.Direction.INCOMING)
    private Set<PostNode> posts = new HashSet<>();

    public HashTagNode(String content) {
        this.content = content;
    }

    public static List<HashTagNode> of(List<String> content, PostNode postNode) {
        return content.stream().map(c -> {
            HashTagNode node = new HashTagNode(c);
            node.addRelationWithPost(postNode);
            return node;
        }).collect(Collectors.toList());
    }

    public void addRelationWithPost(PostNode postNode) {
        this.posts.add(postNode);
    }
}
