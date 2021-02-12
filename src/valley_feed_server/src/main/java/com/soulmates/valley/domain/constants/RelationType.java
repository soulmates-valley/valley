package com.soulmates.valley.domain.constants;

import org.neo4j.graphdb.RelationshipType;

public enum RelationType implements RelationshipType {
    COMMENT,
    LIKE,
    POSTED_LAST,
    PREVIOUS,
    WRITE,
    COMMENTED,
    TIMELINE_,
    INTERESTED_IN
}
