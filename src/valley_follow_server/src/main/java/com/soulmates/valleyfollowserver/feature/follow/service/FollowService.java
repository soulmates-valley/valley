package com.soulmates.valleyfollowserver.feature.follow.service;

import com.soulmates.valleyfollowserver.common.constants.ErrorEnum;
import com.soulmates.valleyfollowserver.common.event.EventSender;
import com.soulmates.valleyfollowserver.common.exception.CustomException;
import com.soulmates.valleyfollowserver.domain.constants.RelationDirection;
import com.soulmates.valleyfollowserver.domain.model.UserNode;
import com.soulmates.valleyfollowserver.domain.repository.TimeLineGraphRepository;
import com.soulmates.valleyfollowserver.domain.repository.UserGraphRepository;
import com.soulmates.valleyfollowserver.feature.follow.dto.FollowCount;
import com.soulmates.valleyfollowserver.feature.follow.dto.FollowInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowService {
    private final UserGraphRepository userRepository;
    private final TimeLineGraphRepository timeLineRepository;
    private final EventSender eventSender;

    @Transactional
    public void addFollow(Long fromUserId, Long toUserId) {
        if (fromUserId.equals(toUserId))
            throw new CustomException(ErrorEnum.FOLLOW_SAME_USER);

        userRepository.findById(fromUserId)
                .orElseThrow(() -> new CustomException(ErrorEnum.USER_NOT_FOUND));
        UserNode toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new CustomException(ErrorEnum.USER_NOT_FOUND));

        if (userRepository.isFollowed(fromUserId, toUserId)) {
            throw new CustomException(ErrorEnum.FOLLOW_ALREADY);
        }

        // request follow
        userRepository.addFollow(fromUserId, toUserId);
        eventSender.sendFollowCreateEvent(fromUserId, toUserId);

        /* search for insert index within Timeline */
        LocalDateTime toUserLastPostDt = toUser.getLastPostDt();
        // check if followed user has not posted a post
        if (toUserLastPostDt == null) {
            return;
        }

        Optional<UserNode> firstUser = timeLineRepository.getNextUserOnLine(fromUserId, fromUserId);
        // check if there is no user in timeline
        if (firstUser.isEmpty()) {
            timeLineRepository.addTimeLineRelation(fromUserId, fromUserId, toUserId);
            return;
        }

        Optional<UserNode> prevUser = timeLineRepository.findLastUserUnderPostTimeOnLine(fromUserId, toUserLastPostDt);
        // when no user has posted more recent posts than followed user on the timeline.
        if (prevUser.isEmpty()) {
            timeLineRepository.deleteTimeLineRelation(fromUserId, fromUserId, RelationDirection.OUTGOING);
            timeLineRepository.addTimeLineRelation(fromUserId, fromUserId, toUserId);
            timeLineRepository.addTimeLineRelation(fromUserId, toUserId, firstUser.get().getUserId());
            return;
        }

        // insert user on timeline
        Optional<UserNode> nextUser = timeLineRepository.getNextUserOnLine(fromUserId, prevUser.get().getUserId());
        if (nextUser.isPresent()) {
            timeLineRepository.deleteTimeLineRelation(fromUserId, prevUser.get().getUserId(), RelationDirection.OUTGOING);
            timeLineRepository.addTimeLineRelation(fromUserId, toUserId, nextUser.get().getUserId());
        }
        timeLineRepository.addTimeLineRelation(fromUserId, prevUser.get().getUserId(), toUserId);
    }


    @Transactional
    public void deleteFollow(Long fromUserId, Long toUserId) {
        if (fromUserId.equals(toUserId))
            throw new CustomException(ErrorEnum.FOLLOW_SAME_USER);

        userRepository.findById(fromUserId)
                .orElseThrow(() -> new CustomException(ErrorEnum.USER_NOT_FOUND));
        userRepository.findById(toUserId)
                .orElseThrow(() -> new CustomException(ErrorEnum.USER_NOT_FOUND));

        if (!userRepository.isFollowed(fromUserId, toUserId)) {
            throw new CustomException(ErrorEnum.FOLLOW_NOT_FOUND);
        }

        userRepository.deleteFollow(fromUserId, toUserId);

        // connect timeline where followed user disappeared
        Optional<UserNode> prevUser = timeLineRepository.getPrevUserOnLine(fromUserId, toUserId);
        if (prevUser.isEmpty()) {
            return;
        }
        timeLineRepository.deleteTimeLineRelation(fromUserId, toUserId, RelationDirection.INCOMMING);

        Optional<UserNode> nextUser = timeLineRepository.getNextUserOnLine(fromUserId, toUserId);
        if (nextUser.isPresent()) {
            timeLineRepository.deleteTimeLineRelation(fromUserId, toUserId, RelationDirection.OUTGOING);
            timeLineRepository.addTimeLineRelation(fromUserId, prevUser.get().getUserId(), nextUser.get().getUserId());
        }
    }

    public List<FollowInfo> getFollowerList(Long userId, int page, int size) {
        return userRepository.getFollowerList(userId, page * size, size);
    }

    public List<FollowInfo> getFollowedList(Long userId, int page, int size) {
        return userRepository.getFollowedList(userId, page * size, size);
    }

    public FollowCount getFollowCnt(Long userId) {
        UserNode user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorEnum.USER_NOT_FOUND));

        return FollowCount.builder()
                .inCnt(userRepository.getFollowerNum(user.getUserId()))
                .outCnt(userRepository.getFollowingNum(user.getUserId()))
                .build();
    }
}
