package com.soulmates.valley.common.util;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

public class NodeUtil {
    public static Node nextNode(Node sourceNode, RelationshipType edgeType) {
        for (Relationship edge : sourceNode.getRelationships(Direction.OUTGOING, edgeType)) {
            return edge.getEndNode();
        }
        return null;
    }

    public static Node previousNode(Node sourceNode, RelationshipType edgeType) {
        for (Relationship edge : sourceNode.getRelationships(Direction.INCOMING, edgeType)) {
            return edge.getStartNode();
        }
        return null;
    }
}
