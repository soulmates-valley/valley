package com.soulmates.valley.domain.constants;

import org.neo4j.graphdb.Label;

public enum NodeType implements Label {
    User,
    Post,
    Interest,
    Comment;
}
