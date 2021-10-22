package com.soulmates.valley.feature.comment.service;

import com.soulmates.valley.common.constants.ErrorEnum;
import com.soulmates.valley.common.event.EventSender;
import com.soulmates.valley.common.exception.CustomException;
import com.soulmates.valley.domain.model.doc.PostDoc;
import com.soulmates.valley.domain.repository.graph.CommentGraphRepository;
import com.soulmates.valley.feature.comment.dto.CommentAddRequest;
import com.soulmates.valley.feature.comment.dto.CommentInfo;
import com.soulmates.valley.feature.posting.service.PostCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final PostCountService postCountService;
    private final CommentGraphRepository commentRepository;
    private final EventSender eventSender;

    @Transactional
    public CommentInfo addCommentToPost(CommentAddRequest commentAddRequest, Long userId) {
        PostDoc post = postCountService.increaseCommentCnt(commentAddRequest.getPostId());
        CommentInfo commentInfo = commentRepository.addCommentToPost(commentAddRequest.getPostId(), userId, commentAddRequest.getContent())
                .orElseThrow(() -> new CustomException(ErrorEnum.COMMENT_NOT_CREATE));

        eventSender.sendCommentCreateEvent(userId, post.getUserId(), post.getId());
        return commentInfo;
    }

    public List<CommentInfo> getCommentFromPost(Long postId, int page, int size) {
        return commentRepository.getCommentFromPost(postId, page * size, size);
    }
}
