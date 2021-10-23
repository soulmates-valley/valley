package com.soulmates.valley.service;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.common.event.EventSender;
import com.soulmates.valley.common.exception.CustomException;
import com.soulmates.valley.domain.model.PostDoc;
import com.soulmates.valley.domain.repository.CommentGraphRepository;
import com.soulmates.valley.dto.comment.CommentAddRequest;
import com.soulmates.valley.dto.comment.CommentInfo;
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

    /**
     * 게시글 댓글 등록 기능
     *
     * @param commentAddRequest 댓글 정보
     * @param userId 댓글 작성자 userId
     * @return 등록된 댓글 반환
     * @throws CustomException 댓글 생성 실패시 exception 발생
     */
    @Transactional
    public CommentInfo addCommentToPost(CommentAddRequest commentAddRequest, Long userId) {
        CommentInfo commentInfo = commentRepository.addCommentToPost(commentAddRequest.getPostId(), userId, commentAddRequest.getContent())
                .orElseThrow(() -> new CustomException(ResponseCode.COMMENT_NOT_CREATE));

        PostDoc post = postCountService.increaseCommentCnt(commentAddRequest.getPostId());
        eventSender.sendCommentCreateEvent(userId, post.getUserId(), post.getId());

        return commentInfo;
    }

    /**
     * 게시글 댓글 조회 기능
     *
     * @param postId 조회하고자 하는 post 식별자
     * @param page 조회 page
     * @param size 조회 size
     * @return 댓글 List
     */
    public List<CommentInfo> getCommentFromPost(Long postId, int page, int size) {
        return commentRepository.getCommentFromPost(postId, page * size, size);
    }
}
