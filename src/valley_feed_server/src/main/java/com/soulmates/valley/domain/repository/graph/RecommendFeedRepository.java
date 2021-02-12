package com.soulmates.valley.domain.repository.graph;

import lombok.RequiredArgsConstructor;
import org.neo4j.cypherdsl.core.renderer.Renderer;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class RecommendFeedRepository {
    private final Neo4jClient neo4jClient;
    private final Neo4jTemplate neo4jTemplate;
    private final Renderer cypherRenderer;

    public Collection<Map<String, Object>> getRecommendFeedByInterest(Long userId) {
        String query = "MATCH (u1:User{userId:" + userId + "})-[:INTERESTED_IN]->(i:Interest) " +
                "WITH u1, collect(id(i)) AS u1interest " +
                "MATCH (u2:User)-[:INTERESTED_IN]->(i:Interest) WHERE u1 <> u2 " +
                "WITH u1, u1interest, u2, collect(id(i)) AS u2interest " +
                "WHERE not (u1)-[:FOLLOW]->(u2) " +
                "WITH u1, u1interest, u2, u2interest,gds.alpha.similarity.jaccard(u1interest, u2interest) AS similarity " +
                "WHERE similarity<>0 " +
                "MATCH(u2)-[:PREVIOUS|POSTED_LAST*]->(p:Post) " +
                "WITH u2, p, u1 " +
                "OPTIONAL MATCH (u3:User)-[r:LIKE]->(p) " +
                "WITH u2, p, u1, count(r) as LIKE_COUNT " +
                "OPTIONAL MATCH (c:Comment)-[t:COMMENTED]->(p) " +
                "WITH u2, p, u1, LIKE_COUNT, count(t) as COMMENT_COUNT " +
                "WHERE LIKE_COUNT<>0 OR COMMENT_COUNT<>0 " +
                "WITH u2, p, u1, COMMENT_COUNT, LIKE_COUNT, LIKE_COUNT*5 + COMMENT_COUNT AS POINT, (duration.inDays(p.createDt, date.realtime()).days) / 5 AS PASSED_DAYS_POINT " +
                "OPTIONAL MATCH (u1)-[f:LIKE]->(p) " +
                "RETURN p.postId as postId, p.createDt as createDt, u2.userId as userId, CASE WHEN f IS NOT NULL THEN true ELSE false END AS isUserLiked, POINT - PASSED_DAYS_POINT AS finalPoint " +
                "ORDER BY POINT - PASSED_DAYS_POINT DESC LIMIT 10 ";

        return neo4jClient.query(query).fetch().all();
    }

    public Collection<Map<String, Object>> getRecommendFeedByFollow(Long userId, String graphName) {
        String query1 = "CALL gds.graph.create('" + graphName + "', 'User', 'FOLLOW'); ";
        String query2 = "MATCH (u1:User{userId:" + userId + "}) " +
                "CALL gds.pageRank.stream('"+graphName+"', { " +
                "  maxIterations: 20, " +
                "  dampingFactor: 0.85, " +
                "  sourceNodes:[u1] " +
                "}) " +
                "YIELD nodeId, score " +
                "WITH u1, gds.util.asNode(nodeId) AS u2, score " +
                "ORDER BY score DESC LIMIT 10 " +
                "WHERE not (gds.util.asNode(nodeId).userId = " + userId + " OR (u1)-[:FOLLOW]->(u2)) " +
                "WITH u1, u2 " +
                "MATCH(u2)-[:PREVIOUS|POSTED_LAST*]->(p:Post) " +
                "WITH u2, p, u1 " +
                "OPTIONAL MATCH (u3:User)-[r:LIKE]->(p) " +
                "WITH u2, p, u1, count(r) as LIKE_COUNT " +
                "OPTIONAL MATCH (c:Comment)-[t:COMMENTED]->(p) " +
                "WITH u2, p, u1, LIKE_COUNT, count(t) as COMMENT_COUNT " +
                "WHERE LIKE_COUNT<>0 OR COMMENT_COUNT<>0 " +
                "WITH u2, p, u1, COMMENT_COUNT, LIKE_COUNT, LIKE_COUNT*5 + COMMENT_COUNT AS POINT, (duration.inDays(p.createDt, date.realtime()).days) / 5 AS PASSED_DAYS_POINT " +
                "OPTIONAL MATCH (u1)-[f:LIKE]->(p) " +
                "RETURN p.postId as postId, p.createDt as createDt, u2.userId as userId, CASE WHEN f IS NOT NULL THEN true ELSE false END AS isUserLiked,  POINT - PASSED_DAYS_POINT AS finalPoint " +
                "ORDER BY POINT - PASSED_DAYS_POINT DESC LIMIT 10; ";
        String query3 = "CALL gds.graph.drop('" + graphName + "'); ";

        neo4jClient.query(query1).fetch().one();
        Collection<Map<String, Object>> result = neo4jClient.query(query2).fetch().all();
        neo4jClient.query(query3).fetch().one();
        return result;
    }
}
