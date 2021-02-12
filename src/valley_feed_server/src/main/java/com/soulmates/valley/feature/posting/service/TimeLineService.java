package com.soulmates.valley.feature.posting.service;

import com.soulmates.valley.domain.constants.RelationDirection;
import com.soulmates.valley.domain.model.graph.UserNode;
import com.soulmates.valley.domain.repository.graph.TimeLineGraphRepository;
import com.soulmates.valley.domain.repository.graph.UserGraphRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TimeLineService {
    private final UserGraphRepository userGraphRepository;
    private final TimeLineGraphRepository timeLineRepository;

    @Transactional
    public void updateTimeLine(UserNode user) {
        List<UserNode> followers = userGraphRepository.findFollowedByUserId(user.getUserId());

        //loop all followers for update timeline
        for (UserNode follower : followers) {
            Long timeLineUserId = follower.getUserId();

            Optional<UserNode> prevUser = timeLineRepository.getPrevUserOnLine(timeLineUserId, user.getUserId());
            Optional<UserNode> nextUser = timeLineRepository.getNextUserOnLine(timeLineUserId, user.getUserId());
            // connect line where the user disappeared on timeline
            if (prevUser.isPresent()) {
                timeLineRepository.deleteTimeLineRelation(timeLineUserId, user.getUserId(), RelationDirection.INCOMMING);
                if (nextUser.isPresent()) {
                    timeLineRepository.deleteTimeLineRelation(timeLineUserId, user.getUserId(), RelationDirection.OUTGOING);
                    timeLineRepository.addTimeLineRelation(timeLineUserId, prevUser.get().getUserId(), nextUser.get().getUserId());
                }
            }

            // insert user at the beginning of timeline
            Optional<UserNode> firstUser = timeLineRepository.getNextUserOnLine(timeLineUserId, timeLineUserId);
            if (firstUser.isPresent()) {
                timeLineRepository.deleteTimeLineRelation(timeLineUserId, timeLineUserId, RelationDirection.OUTGOING);
                timeLineRepository.addTimeLineRelation(timeLineUserId, user.getUserId(), firstUser.get().getUserId());
            }
            timeLineRepository.addTimeLineRelation(timeLineUserId, timeLineUserId, user.getUserId());
        }
    }
}
