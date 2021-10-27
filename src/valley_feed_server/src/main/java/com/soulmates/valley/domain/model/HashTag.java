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
public class HashTag {

    @Id
    private String content;

    @Relationship(type = "TAG", direction = Relationship.Direction.INCOMING)
    private Set<Post> posts = new HashSet<>();

    public HashTag(String content) {
        this.content = content;
    }

    public static List<HashTag> of(List<String> content, Post post) {
        return content.stream().map(c -> {
            HashTag node = new HashTag(c);
            node.addRelationWithPost(post);
            return node;
        }).collect(Collectors.toList());
    }

    public void addRelationWithPost(Post post) {
        this.posts.add(post);
    }
}
