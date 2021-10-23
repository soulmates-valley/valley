package com.soulmates.valley.service;

import com.soulmates.valley.domain.model.PostDoc;
import com.soulmates.valley.domain.repository.PostDocRepository;
import com.soulmates.valley.domain.repository.RecommendFeedRepository;
import com.soulmates.valley.common.util.FeedCombiner;
import com.soulmates.valley.dto.posting.PostDetail;
import com.soulmates.valley.dto.posting.PostInfo;
import com.soulmates.valley.common.util.PostCombiner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecommendFeedService {

    private final RecommendFeedRepository recommendFeedRepository;
    private final PostDocRepository postDocRepository;
    private final FeedCombiner feedCombiner;
    private final RedisTemplate<String, PostInfo> redisTemplate;

    public List<PostDetail> getRecommendFeedByInterest(Long userId) {
        final String FEED_KEY = "recommend:" + userId;

        List<PostInfo> postInfoList = redisTemplate.opsForList().range(FEED_KEY, 0, -1);

        // if not exist cache in redis
        if (postInfoList == null || postInfoList.isEmpty()) {
            Collection<Map<String, Object>> feedInterestTemp = recommendFeedRepository.getRecommendFeedByInterest(userId);
            Collection<Map<String, Object>> feedFollowTemp = recommendFeedRepository.getRecommendFeedByFollow(userId, "feed" + userId);
            postInfoList = feedCombiner.combineFeed(feedInterestTemp, feedFollowTemp);
            // post info caching
            redisTemplate.opsForList().leftPushAll(FEED_KEY, postInfoList);
            redisTemplate.expire(FEED_KEY, 5, TimeUnit.MINUTES);
        }

        List<Long> postIdList = postInfoList.stream().map(PostInfo::getPostId).collect(Collectors.toList());
        List<PostDoc> postDocList = postDocRepository.findAllByIdIn(postIdList);
        return PostCombiner.combinePostList(postDocList, postInfoList);
    }
}
