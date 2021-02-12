package com.soulmates.valley.domain.repository.graph;

import com.soulmates.valley.domain.model.graph.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface UserGraphRepository extends Neo4jRepository<UserNode, Long> {
    @Query("MATCH (p:Post{postId:$postId})<-[:PREVIOUS|POSTED_LAST*]-(u:User) RETURN u ")
    Optional<UserNode> findOwnerByPostId(Long postId);

    @Query("MATCH (user:User{userId:$userId}) RETURN user ")
    Optional<UserNode> findUserByUserId(Long userId);

    @Query("MATCH (:User{userId: $userId})<-[:FOLLOW]-(f:User) return collect(f) ")
    List<UserNode> findFollowedByUserId(Long userId);
}
