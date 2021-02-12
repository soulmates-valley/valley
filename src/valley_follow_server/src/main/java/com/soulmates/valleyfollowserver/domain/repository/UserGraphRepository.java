package com.soulmates.valleyfollowserver.domain.repository;

import com.soulmates.valleyfollowserver.domain.model.UserNode;
import com.soulmates.valleyfollowserver.feature.follow.dto.FollowInfo;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface UserGraphRepository extends Neo4jRepository<UserNode, Long> {
    @Query("MATCH (t:User{userId:$toUserId}) WITH t " +
            "MATCH (f:User{userId:$fromUserId}) WITH t,f " +
            "MERGE (f)-[r:FOLLOW]->(t)")
    void addFollow(Long fromUserId, Long toUserId);

    @Query("MATCH (t:User{userId:$toUserId}) WITH t " +
            "MATCH (f:User{userId:$fromUserId}) WITH t,f " +
            "MATCH (f)-[r:FOLLOW]->(t) DELETE r")
    void deleteFollow(Long fromUserId, Long toUserId);

    @Query("MATCH (t:User{userId:$userId})<-[:FOLLOW]-(f:User) " +
            "OPTIONAL MATCH (t)-[r:FOLLOW]->(f) RETURN f, " +
            "CASE WHEN r IS NULL THEN false ELSE true END AS isFollowed " +
            "ORDER BY f.nickname DESC SKIP $page LIMIT $size ")
    List<FollowInfo> getFollowerList(Long userId, int page, int size);

    @Query("MATCH (t:User{userId: $userId})-[r:FOLLOW]->(f:User) " +
            "RETURN f, true as isFollowed " +
            "ORDER BY f.nickname DESC SKIP $page LIMIT $size ")
    List<FollowInfo> getFollowedList(Long userId, int page, int size);

    @Query("MATCH (f:User{userId:$userId})<-[:FOLLOW]-(t:User) " +
            "RETURN COUNT(t)")
    int getFollowerNum(Long userId);

    @Query("MATCH (f:User{userId:$userId})-[:FOLLOW]->(t:User) " +
            "RETURN COUNT(t)")
    int getFollowingNum(Long userId);

    @Query("OPTIONAL MATCH (f:User{userId:$fromUserId})-[r:FOLLOW]->(t:User{userId:$toUserId}) " +
            "RETURN CASE WHEN r IS NOT NULL THEN true ELSE false END ")
    boolean isFollowed(Long fromUserId, Long toUserId);
}
