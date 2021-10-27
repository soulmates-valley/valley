package com.soulmates.valley.domain.repository;

import com.soulmates.valley.domain.model.HashTag;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface HashTagRepository extends Neo4jRepository<HashTag, String> {
}
