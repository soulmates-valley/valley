package com.soulmates.valley.service;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.common.event.EventSender;
import com.soulmates.valley.common.exception.CustomException;
import com.soulmates.valley.domain.constants.RelationDirection;
import com.soulmates.valley.dto.posting.PostAddRequest;
import com.soulmates.valley.dto.posting.PostDetail;
import com.soulmates.valley.dto.posting.PostInfo;
import com.soulmates.valley.dto.posting.PostLimitRequest;
import com.soulmates.valley.domain.model.PostDoc;
import com.soulmates.valley.domain.model.User;
import com.soulmates.valley.common.util.S3Uploader;
import com.soulmates.valley.domain.repository.PostDocRepository;
import com.soulmates.valley.domain.repository.TimeLineRepository;
import com.soulmates.valley.domain.repository.UserRepository;
import com.soulmates.valley.domain.repository.PostRepository;
import com.soulmates.valley.common.util.post.PostConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class PostInfoService {

    private final PostRepository postRepository;
    private final PostDocRepository postDocRepository;
    private final UserRepository userRepository;
    private final TimeLineRepository timeLineRepository;
    private final S3Uploader s3Uploader;
    private final EventSender eventSender;
    private final PostConverter postConverter;

    private static final long NO_MAX_POST_ID = -1L;

    /**
     * 게시글 등록 기능
     *
     * @param userId 게시글 등록 user 식별자
     * @param postAddRequest 게시글 등록 정보
     * @return 게시글 정보
     * @throws CustomException user 존재하지 않으면 exception 발생
     */
    @Transactional
    public PostDoc addPost(Long userId, PostAddRequest postAddRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ResponseCode.USER_NOT_FOUND));

        List<String> imageList = new LinkedList<>();
        if (postAddRequest.isExistImages()) {
            imageList = s3Uploader.upload(postAddRequest.getImages());
        }

        PostDoc post = postDocRepository.save(PostDoc.of(postAddRequest, imageList, user));
        postRepository.addNewPost(userId, post.getId());

        eventSender.sendPostAndHashTagCreateEvent(post);
        updateTimeLine(user);

        return post;
    }

    /**
     * 특정 user가 작성한 게시글 조회
     * cursor-based 페이징 방식 적용
     *
     * @param userId user 식별자
     * @param postLimitRequest 게시글 조회 정보
     * @return user가 작성한 게시글 List
     */
    public List<PostDetail> getUserPostList(Long userId, PostLimitRequest postLimitRequest) {
        List<PostInfo> postInfoList;
        if (postLimitRequest.getMaxPostId() == null || postLimitRequest.getMaxPostId() == NO_MAX_POST_ID) {
            postInfoList = timeLineRepository.getUserPostInfoFirst(postLimitRequest.getSearchUserId(), userId, postLimitRequest.getSize());
        } else {
            postInfoList = timeLineRepository.getUserPostInfo(postLimitRequest.getSearchUserId(), userId,
                    postLimitRequest.getMaxPostId(), postLimitRequest.getSize());
        }

        return postConverter.convertToPost(postInfoList);
    }

    /**
     * 게시글 상세정보 조회
     *
     * @param postId post 식별자
     * @param userId 조회 요청한 user 식별자 (좋아요 여부 확인 용도)
     * @return 게시글 상세정보
     * @throws CustomException 게시글이 존재하지 않으면 exception 발생
     */
    public PostDetail getPostDetail(Long postId, Long userId) {
        PostDoc postDoc = postDocRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ResponseCode.POST_NOT_FOUND));
        boolean isLiked = postRepository.isLikedByUserId(postId, userId);
        return PostDetail.of(postDoc, isLiked);
    }


    private void updateTimeLine(User user) {
        List<User> followers = userRepository.findFollowedByUserId(user.getUserId());

        //loop all followers for update timeline
        for (User follower : followers) {
            Long timeLineUserId = follower.getUserId();

            Optional<User> prevUser = timeLineRepository.getPrevUserOnLine(timeLineUserId, user.getUserId());
            Optional<User> nextUser = timeLineRepository.getNextUserOnLine(timeLineUserId, user.getUserId());
            // connect line where the user disappeared on timeline
            if (prevUser.isPresent()) {
                timeLineRepository.deleteTimeLineRelation(timeLineUserId, user.getUserId(), RelationDirection.INCOMMING);
                if (nextUser.isPresent()) {
                    timeLineRepository.deleteTimeLineRelation(timeLineUserId, user.getUserId(), RelationDirection.OUTGOING);
                    timeLineRepository.addTimeLineRelation(timeLineUserId, prevUser.get().getUserId(), nextUser.get().getUserId());
                }
            }

            // insert user at the beginning of timeline
            Optional<User> firstUser = timeLineRepository.getNextUserOnLine(timeLineUserId, timeLineUserId);
            if (firstUser.isPresent()) {
                timeLineRepository.deleteTimeLineRelation(timeLineUserId, timeLineUserId, RelationDirection.OUTGOING);
                timeLineRepository.addTimeLineRelation(timeLineUserId, user.getUserId(), firstUser.get().getUserId());
            }
            timeLineRepository.addTimeLineRelation(timeLineUserId, timeLineUserId, user.getUserId());
        }
    }
}
