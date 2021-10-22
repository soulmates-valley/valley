package com.soulmates.valley.feature.posting.service;

import com.soulmates.valley.common.constants.ErrorEnum;
import com.soulmates.valley.common.event.EventSender;
import com.soulmates.valley.common.exception.CustomException;
import com.soulmates.valley.feature.posting.util.PostMapper;
import com.soulmates.valley.domain.model.doc.PostDoc;
import com.soulmates.valley.domain.model.graph.UserNode;
import com.soulmates.valley.common.util.S3Uploader;
import com.soulmates.valley.domain.repository.doc.PostDocRepository;
import com.soulmates.valley.domain.repository.graph.TimeLineGraphRepository;
import com.soulmates.valley.domain.repository.graph.UserGraphRepository;
import com.soulmates.valley.feature.posting.dto.*;
import com.soulmates.valley.domain.repository.graph.PostGraphRepository;
import com.soulmates.valley.feature.posting.util.PostCombiner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostGraphRepository postGraphRepository;
    private final PostDocRepository postDocRepository;
    private final TimeLineService timeLineService;
    private final UserGraphRepository userGraphRepository;
    private final TimeLineGraphRepository timeLineGraphRepository;
    private final PostMapper postMapper;
    private final S3Uploader s3Uploader;
    private final EventSender eventSender;

    private final static long NO_MAX_POST_ID = -1L;

    @Transactional
    public PostDoc addPost(Long userId, PostAddRequest postAddRequest) {
        UserNode user = userGraphRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorEnum.USER_NOT_FOUND));

        List<String> imageList = new LinkedList<>();
        if (postAddRequest.isExistImages()) {
            imageList = s3Uploader.upload(postAddRequest.getImages());
        }

        PostDoc post = postDocRepository.save(PostDoc.of(postAddRequest, imageList, user));
        postGraphRepository.addNewPost(userId, post.getId());

        eventSender.sendPostAndHashTagCreateEvent(post);

        //update timeline for all followers
        timeLineService.updateTimeLine(user);
        return post;
    }

    public List<PostDetail> getUserPostList(Long userId, PostLimitRequest postLimitRequest) {
        Collection<Map<String, Object>> postInfo;
        if (postLimitRequest.getMaxPostId() == null || postLimitRequest.getMaxPostId() == NO_MAX_POST_ID) {
            postInfo = timeLineGraphRepository.getUserPostInfoFirst(postLimitRequest.getSearchUserId(), userId, postLimitRequest.getSize());
        } else {
            postInfo = timeLineGraphRepository.getUserPostInfo(postLimitRequest.getSearchUserId(), userId,
                    postLimitRequest.getMaxPostId(), postLimitRequest.getSize());
        }


        List<PostInfo> postInfoList = postMapper.convertPostList(postInfo);
        List<Long> postIdList = postInfoList.stream().map(PostInfo::getPostId).collect(Collectors.toList());
        List<PostDoc> postDocList = postDocRepository.findAllByIdIn(postIdList);
        return PostCombiner.combinePostList(postDocList, postInfoList);
    }

    public PostDetail getPostDetail(Long postId, Long userId) {
        PostDoc postDoc = postDocRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorEnum.POST_NOT_FOUND));
        boolean isLiked = postGraphRepository.isLikedByUserId(postId, userId);
        return PostDetail.of(postDoc, isLiked);
    }
}
