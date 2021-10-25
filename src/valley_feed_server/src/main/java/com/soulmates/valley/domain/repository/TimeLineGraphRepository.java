package com.soulmates.valley.domain.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.common.exception.CustomException;
import com.soulmates.valley.domain.constants.RelationDirection;
import com.soulmates.valley.domain.constants.NodeType;
import com.soulmates.valley.domain.constants.RelationType;
import com.soulmates.valley.domain.model.UserNode;
import com.soulmates.valley.dto.posting.PostInfo;
import lombok.RequiredArgsConstructor;
import org.neo4j.cypherdsl.core.*;
import org.neo4j.cypherdsl.core.renderer.Renderer;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class TimeLineGraphRepository {

    private final Neo4jClient neo4jClient;
    private final Neo4jTemplate neo4jTemplate;
    private final Renderer cypherRenderer;
    private final ObjectMapper objectMapper;

    public Optional<UserNode> getNextUserOnLine(Long timeLineUserId, Long userId) {
        Node user = Cypher.node(NodeType.User.name())
                .named("user")
                .withProperties("userId", Cypher.literalOf(userId));
        Node nextUser = Cypher.anyNode("nextUser");

        Statement statement = Cypher
                .match(user.relationshipTo(nextUser, RelationType.TIMELINE_.name() + timeLineUserId))
                .returning(nextUser).build();

        return neo4jTemplate.findOne(statement, new HashMap<>(), UserNode.class);
    }

    public Optional<UserNode> getPrevUserOnLine(Long timeLineUserId, Long userId) {
        Node user = Cypher.node(NodeType.User.name())
                .named("user")
                .withProperties("userId", Cypher.literalOf(userId));
        Node prevUser = Cypher.anyNode("prevUser");

        Statement statement = Cypher
                .match(user.relationshipFrom(prevUser, RelationType.TIMELINE_.name() + timeLineUserId))
                .returning(prevUser).build();

        return neo4jTemplate.findOne(statement, new HashMap<>(), UserNode.class);
    }

    public void addTimeLineRelation(Long timeLineUserId, Long fromUserId, Long toUserId) {
        Node fromUser = Cypher.node(NodeType.User.name())
                .named("fromUser")
                .withProperties("userId", Cypher.literalOf(fromUserId));
        Node toUser = Cypher.node(NodeType.User.name())
                .named("toUser")
                .withProperties("userId", Cypher.literalOf(toUserId));

        Statement statement = Cypher
                .match(fromUser).with(fromUser)
                .match(toUser).with(fromUser, toUser)
                .merge(fromUser.relationshipTo(toUser, RelationType.TIMELINE_.name() + timeLineUserId)).build();

        neo4jClient.query(cypherRenderer.render(statement)).fetch().one();
    }

    public void deleteTimeLineRelation(Long timeLineUserId, Long userId, RelationDirection direction) {
        String query = "";
        if (direction == RelationDirection.INCOMMING) {
            query = "MATCH (f:User{userId:" + userId + "})<-[timeline:TIMELINE_" + timeLineUserId + "]-(:User) delete timeline ";
        } else if (direction == RelationDirection.OUTGOING) {
            query = "MATCH (f:User{userId:" + userId + "})-[timeline:TIMELINE_" + timeLineUserId + "]->(:User) delete timeline ";
        } else {
            throw new CustomException(ResponseCode.PARAM_INVALID);
        }
        neo4jClient.query(query).fetch().one();
    }

    public List<PostInfo> getFeedPostInfo(Long userId, int length, LocalDateTime fromDate) {
        String query = "MATCH p=(me:User{ userId: " + userId + "})-[:TIMELINE_" + userId + "*1.." + length + "]->(friend:User) WITH me, friend " +
                "MATCH (friend)-[:POSTED_LAST|PREVIOUS*]->(post) " +
                "WHERE post.createDt > localdatetime('" + fromDate + "') WITH post, me, friend " +
                "OPTIONAL MATCH (me)-[like:LIKE]->(post) " +
                "RETURN post.createDt as createDt, post.postId as postId, friend.userId as userId, " +
                "CASE WHEN like IS NULL THEN false ELSE true END AS isUserLiked " +
                "ORDER BY post.createDt DESC LIMIT " + length;

        Collection<Map<String, Object>> result = neo4jClient.query(query).fetch().all();
        return convertPostList(result);
    }

    public List<PostInfo> getUserPostInfo(Long searchUserId, Long userId, Long postId, int size){
        String query = "MATCH (searchUser:User{userId:"+searchUserId+"})-[:POSTED_LAST|PREVIOUS*]->(firstPost:Post) " +
                "WHERE firstPost.postId = "+postId+" WITH firstPost, searchUser " +
                "MATCH (firstPost)-[:POSTED_LAST|PREVIOUS*1.."+size+"]->(post:Post) " +
                "OPTIONAL MATCH (user:User{userId:"+userId+"})-[like:LIKE]->(post) " +
                "RETURN post.postId as postId, post.createDt as createDt, searchUser.userId as userId, CASE WHEN like IS NOT NULL THEN true ELSE false END AS isUserLiked ";

        Collection<Map<String, Object>> result = neo4jClient.query(query).fetch().all();
        return convertPostList(result);
    }

    public List<PostInfo> getUserPostInfoFirst(Long searchUserId, Long userId, int size){
        String query = "MATCH (searchUser:User{userId:"+searchUserId+"})-[:POSTED_LAST|PREVIOUS*1.."+size+"]->(post:Post) " +
                "OPTIONAL MATCH (user:User{userId:"+userId+"})-[like:LIKE]->(post) " +
                "RETURN post.postId as postId, post.createDt as createDt, searchUser.userId as userId, CASE WHEN like IS NOT NULL THEN true ELSE false END AS isUserLiked ";

        Collection<Map<String, Object>> result = neo4jClient.query(query).fetch().all();
        return convertPostList(result);
    }

    private List<PostInfo> convertPostList(Collection<Map<String, Object>> postList) {
        return postList.stream().map(m -> {
            PostInfo postInfo = objectMapper.convertValue(m, PostInfo.class);
            postInfo.setUserLiked((boolean) m.get("isUserLiked"));
            return postInfo;
        }).collect(Collectors.toList());
    }
}
