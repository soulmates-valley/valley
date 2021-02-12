package com.soulmates.valleyfollowserver.domain.repository;

import com.soulmates.valleyfollowserver.common.constants.ErrorEnum;
import com.soulmates.valleyfollowserver.common.exception.CustomException;
import com.soulmates.valleyfollowserver.domain.constants.NodeType;
import com.soulmates.valleyfollowserver.domain.constants.RelationDirection;
import com.soulmates.valleyfollowserver.domain.constants.RelationType;
import com.soulmates.valleyfollowserver.domain.model.UserNode;
import lombok.RequiredArgsConstructor;
import org.neo4j.cypherdsl.core.Cypher;
import org.neo4j.cypherdsl.core.Functions;
import org.neo4j.cypherdsl.core.Node;
import org.neo4j.cypherdsl.core.Statement;
import org.neo4j.cypherdsl.core.renderer.Renderer;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TimeLineGraphRepository {
    private final Neo4jClient neo4jClient;
    private final Neo4jTemplate neo4jTemplate;
    private final Renderer cypherRenderer;

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

    public Optional<UserNode> findLastUserUnderPostTimeOnLine(Long timeLineUserId, LocalDateTime postedDt) {
        Node fromUser = Cypher.node(NodeType.User.name())
                .named("fromUser")
                .withProperties("userId", Cypher.literalOf(timeLineUserId));
        Node toUser = Cypher.node(NodeType.User.name())
                .named("toUser");
        var lastPostDt = toUser.property("lastPostDt");

        Statement statement = Cypher
                .match(fromUser.relationshipTo(toUser, RelationType.TIMELINE_.name() + timeLineUserId).length(1, Integer.MAX_VALUE))
                .where(lastPostDt.gt(Functions.localdatetime(postedDt.toString())))
                .returning(toUser).orderBy(toUser.property("lastPostDt").ascending()).limit(1).build();

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
            throw new CustomException(ErrorEnum.PARAM_INVALID);
        }

        neo4jClient.query(query).fetch().one();
    }
}
