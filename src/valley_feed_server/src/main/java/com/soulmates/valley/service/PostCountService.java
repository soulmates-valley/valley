package com.soulmates.valley.service;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.domain.model.PostDoc;
import com.soulmates.valley.common.exception.CustomException;
import com.soulmates.valley.domain.repository.PostDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostCountService {

    private final PostDocRepository postDocRepository;

    /**
     * 포스트 좋아요 총개수 증가
     *
     * @param postId 게시글 식별자
     * @return 게시글 정보
     */
    public PostDoc increaseLikeCnt(Long postId) {
        PostDoc post = postDocRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ResponseCode.POST_NOT_FOUND));
        post.increaseLikeCnt();
        postDocRepository.save(post);
        return post;
    }

    /**
     * 포스트 좋아요 총개수 감소
     *
     * @param postId 게시글 식별자
     * @return 게시글 정보
     */

    public PostDoc decreaseLikeCnt(Long postId) {
        PostDoc post = postDocRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ResponseCode.POST_NOT_FOUND));
        if (post.getLikeCnt() == 0) {
            throw new CustomException(ResponseCode.LIKE_UNDER_ZERO);
        }
        post.decreaseLikeCnt();
        postDocRepository.save(post);
        return post;
    }

    /**
     * 포스트 댓글 총개수 증가
     *
     * @param postId 게시글 식별자
     * @return 게시글 정보
     */
    public PostDoc increaseCommentCnt(Long postId) {
        PostDoc post = postDocRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ResponseCode.POST_NOT_FOUND));
        post.increaseCommentCnt();
        postDocRepository.save(post);
        return post;
    }

    /**
     * 포스트 댓글 총개수 감소
     *
     * @param postId 게시글 식별자
     * @return 게시글 정보
     */
    public PostDoc decreaseCommentCnt(Long postId) {
        PostDoc post = postDocRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ResponseCode.POST_NOT_FOUND));
        post.decreaseCommentCnt();
        postDocRepository.save(post);
        return post;
    }
}
