package com.soulmates.valley.domain.repository;

import com.soulmates.valley.domain.model.PostNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;

public interface PostGraphRepository extends Neo4jRepository<PostNode, Long> {

    @Query("MATCH (u:User{userId:$userId})-[:POSTED_LAST]->(p:Post) " +
            "RETURN p ")
    Optional<PostNode> getLastPostByUserId(Long userId);

    @Query("MATCH(user:User{userId:$userId}) " +
            "OPTIONAL MATCH (user)-[last:POSTED_LAST]->(oldPost:Post) " +
            "DELETE (last) " +
            "CREATE (user)-[:POSTED_LAST]->(post:Post{postId:$postId}) WITH user, post, oldPost " +
            "FOREACH(iter IN CASE WHEN oldPost IS NOT NULL THEN [1] ELSE [] END | MERGE (post)-[:PREVIOUS]->(oldPost)) " +
            "SET user.lastPostDt = localdatetime({ timezone: 'Asia/Seoul' }), post.createDt = localdatetime({ timezone: 'Asia/Seoul' }) RETURN post")
    PostNode addNewPost(Long userId, Long postId);

    @Query("MATCH (u:User{userId: $userId})-[r:POSTED_LAST]->(p:Post{postId:$postId}) " +
            "DELETE r ")
    void deletePostedLastRelation(Long userId, Long postId);

    @Query("MATCH (f:Post{postId: $fromPostId}) WITH f " +
            "MATCH (t:Post{postId: $toPostId}) WITH f, t " +
            "CREATE (f)-[:PREVIOUS]->(t) ")
    void addPreviousRelation(Long fromPostId, Long toPostId);

    @Query("MATCH (p:Post{postId:$postId}) WITH p " +
            "MATCH (u:User{userId:$userId}) " +
            "MERGE (u)-[:LIKE]->(p) ")
    void addLikeToPost(Long postId, Long userId);

    @Query("MATCH (n:Post{postId:$postId})<-[r:LIKE]-(u:User{userId:$userId}) " +
            "DELETE r ")
    void deleteLikeToPost(Long postId, Long userId);

    @Query("OPTIONAL MATCH (n:User{userId:$userId})-[f:LIKE]->(:Post{postId:$postId}) " +
            "RETURN CASE WHEN f IS NOT NULL THEN true ELSE false END")
    boolean isLikedByUserId(Long postId, Long userId);
}
