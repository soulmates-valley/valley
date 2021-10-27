package com.soulmates.valley.domain.repository;

import com.soulmates.valley.domain.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User, Long> {

    @Query("MATCH (p:Post{postId:$postId})<-[:PREVIOUS|POSTED_LAST*]-(u:User) RETURN u ")
    Optional<User> findOwnerByPostId(Long postId);

    @Query("MATCH (user:User{userId:$userId}) RETURN user ")
    Optional<User> findUserByUserId(Long userId);

    @Query("MATCH (:User{userId: $userId})<-[:FOLLOW]-(f:User) return collect(f) ")
    List<User> findFollowedByUserId(Long userId);
}
