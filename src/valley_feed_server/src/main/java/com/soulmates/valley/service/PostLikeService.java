package com.soulmates.valley.service;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.common.event.EventSender;
import com.soulmates.valley.common.exception.CustomException;
import com.soulmates.valley.domain.model.UserNode;
import com.soulmates.valley.domain.repository.PostGraphRepository;
import com.soulmates.valley.domain.repository.UserGraphRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostLikeService {

    private final PostGraphRepository postGraphRepository;
    private final PostCountService postCountService;
    private final UserGraphRepository userGraphRepository;
    private final EventSender eventSender;

    /**
     * 포스트 좋아요 기능
     *
     * @param postId 좋아요 요청한 post 식별자
     * @param userId 좋아요 요청한 user 식별자
     * @throws CustomException postId에 해당하는 user 정보가 없으면 exception 발생
     */
    @Transactional
    public void addLikeToPost(Long postId, Long userId) {
        postCountService.increaseLikeCnt(postId);
        UserNode postOwner = userGraphRepository.findOwnerByPostId(postId)
                .orElseThrow(() -> new CustomException(ResponseCode.POST_OWNER_NOT_FOUND));

        eventSender.sendLikeCreateEvent(userId, postOwner.getUserId(), postId);
        postGraphRepository.addLikeToPost(postId, userId);
    }

    /**
     * 포스트 좋아요 취소 기능
     *
     * @param postId 좋아요 요청 취소한 post 식별자
     * @param userId 좋아요 요청 취소한 user 식별자
     */
    @Transactional
    public void deleteLikeToPost(Long postId, Long userId) {
        postCountService.decreaseLikeCnt(postId);
        postGraphRepository.deleteLikeToPost(postId, userId);
    }
}
