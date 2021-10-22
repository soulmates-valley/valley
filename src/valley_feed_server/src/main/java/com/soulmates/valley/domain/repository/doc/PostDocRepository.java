package com.soulmates.valley.domain.repository.doc;

import com.soulmates.valley.domain.model.doc.PostDoc;
import com.soulmates.valley.feature.posting.dto.PostDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDocRepository extends MongoRepository<PostDoc, Long> {

    List<PostDoc> findAllByIdIn(List<Long> userIdList);
}
