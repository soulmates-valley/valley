package com.soulmates.valley.domain.repository.graph;

import com.soulmates.valley.domain.model.graph.HashTagNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface HashTagGraphRepository extends Neo4jRepository<HashTagNode, String> {
}
