package com.soulmates.valley.feature.like.service;

import com.soulmates.valley.common.constants.ErrorEnum;
import com.soulmates.valley.common.event.EventSender;
import com.soulmates.valley.common.exception.CustomException;
import com.soulmates.valley.domain.model.graph.UserNode;
import com.soulmates.valley.domain.repository.graph.PostGraphRepository;
import com.soulmates.valley.domain.repository.graph.UserGraphRepository;
import com.soulmates.valley.feature.posting.service.PostCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final PostGraphRepository postGraphRepository;
    private final PostCountService postCountService;
    private final UserGraphRepository userGraphRepository;
    private final EventSender eventSender;

    @Transactional
    public void addLikeToPost(Long postId, Long userId) {
        postCountService.increaseLikeCnt(postId);
        UserNode postOwner = userGraphRepository.findOwnerByPostId(postId)
                .orElseThrow(() -> new CustomException(ErrorEnum.POST_OWNER_NOT_FOUND));

        eventSender.sendLikeCreateEvent(userId, postOwner.getUserId(), postId);
        postGraphRepository.addLikeToPost(postId, userId);
    }

    @Transactional
    public void deleteLikeToPost(Long postId, Long userId) {
        postCountService.decreaseLikeCnt(postId);
        postGraphRepository.deleteLikeToPost(postId, userId);
    }
}
