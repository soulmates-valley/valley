package com.soulmates.valley.domain.repository.graph;

import com.soulmates.valley.domain.model.graph.CommentNode;
import com.soulmates.valley.feature.comment.dto.CommentInfo;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface CommentGraphRepository extends Neo4jRepository<CommentNode, Long> {

    @Query("MATCH (:Post{postId:$postId})<-[:COMMENTED]-(c:Comment)<-[:WRITE]-(u:User)" +
            "RETURN c, u.userId as userId, u.nickname as nickname, u.profileImg as profileImg " +
            "ORDER BY c.createDt ASC SKIP $page LIMIT $size ")
    List<CommentInfo> getCommentFromPost(Long postId, int page, int size);

    @Query("MATCH (p:Post{postId:$postId}) WITH p " +
            "MATCH (u:User{userId:$userId}) " +
            "CREATE (u)-[:WRITE]->(n:Comment{content:$comment, createDt: localdatetime({ timezone: 'Asia/Seoul' })})-[:COMMENTED]->(p) " +
            "RETURN n, u.userId as userId, u.nickname as nickname, u.profileImg as profileImg ")
    Optional<CommentInfo> addCommentToPost(Long postId, Long userId, String comment);
}
