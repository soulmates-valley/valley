package com.soulmates.valley.domain.repository;

import com.soulmates.valley.domain.model.PostDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDocRepository extends MongoRepository<PostDoc, Long> {

    List<PostDoc> findAllByIdIn(List<Long> userIdList);
}