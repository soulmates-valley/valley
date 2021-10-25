package com.soulmates.valley.common.util.post;

import com.soulmates.valley.domain.model.PostDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostListner extends AbstractMongoEventListener<PostDoc> {

    private final SequenceGenerator sequenceGenerator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<PostDoc> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(PostDoc.SEQUENCE_NAME));
        }
    }
}