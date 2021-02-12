package com.soulmates.valley.feature.posting.service;

import com.soulmates.valley.common.constants.ErrorEnum;
import com.soulmates.valley.domain.model.doc.PostDoc;
import com.soulmates.valley.common.exception.CustomException;
import com.soulmates.valley.domain.repository.doc.PostDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostCountService {
    private final PostDocRepository postDocRepository;

    public PostDoc increaseLikeCnt(Long postId) {
        PostDoc post = postDocRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorEnum.POST_NOT_FOUND));
        post.increaseLikeCnt();
        postDocRepository.save(post);
        return post;
    }

    public PostDoc decreaseLikeCnt(Long postId) {
        PostDoc post = postDocRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorEnum.POST_NOT_FOUND));
        if (post.getLikeCnt() == 0) {
            throw new CustomException(ErrorEnum.LIKE_UNDER_ZERO);
        }
        post.decreaseLikeCnt();
        postDocRepository.save(post);
        return post;
    }

    public PostDoc increaseCommentCnt(Long postId) {
        PostDoc post = postDocRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorEnum.POST_NOT_FOUND));
        post.increaseCommentCnt();
        postDocRepository.save(post);
        return post;
    }

    public PostDoc decreaseCommentCnt(Long postId) {
        PostDoc post = postDocRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorEnum.POST_NOT_FOUND));
        post.decreaseCommentCnt();
        postDocRepository.save(post);
        return post;
    }
}
