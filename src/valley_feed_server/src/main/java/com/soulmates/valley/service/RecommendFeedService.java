package com.soulmates.valley.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulmates.valley.common.util.RedisKeyGenerator;
import com.soulmates.valley.domain.model.PostDoc;
import com.soulmates.valley.domain.repository.PostDocRepository;
import com.soulmates.valley.domain.repository.RecommendFeedRepository;
import com.soulmates.valley.dto.posting.PostDetail;
import com.soulmates.valley.dto.posting.PostInfo;
import com.soulmates.valley.common.util.post.PostConverter;
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
    private final RedisTemplate<String, PostInfo> redisTemplate;
    private final ObjectMapper objectMapper;
    private final PostConverter postConverter;

    /**
     * 추천피드 게시글 조회
     *
     * @param userId 조회하고자하는 user 식별자
     * @return 추천피드 게시글 List
     */
    public List<PostDetail> getRecommendFeedByInterest(Long userId) {
        final String FEED_KEY = RedisKeyGenerator.generateKey("recommend", ":", userId);

        List<PostInfo> postInfoList = redisTemplate.opsForList().range(FEED_KEY, 0, -1);

        // if not exist cache in redis
        if (postInfoList == null || postInfoList.isEmpty()) {
            Collection<Map<String, Object>> feedInterestTemp = recommendFeedRepository.getRecommendFeedByInterest(userId);
            Collection<Map<String, Object>> feedFollowTemp = recommendFeedRepository.getRecommendFeedByFollow(userId, "feed" + userId);
            postInfoList = combineFeed(feedInterestTemp, feedFollowTemp);
            // post info caching
            redisTemplate.opsForList().leftPushAll(FEED_KEY, postInfoList);
            redisTemplate.expire(FEED_KEY, 5, TimeUnit.MINUTES);
        }

        return postConverter.convertToPost(postInfoList);
    }

    private List<PostInfo> combineFeed(Collection<Map<String, Object>> feedIntersetTemp, Collection<Map<String, Object>> feedFollowTemp) {
        feedIntersetTemp.addAll(feedFollowTemp);

        return feedIntersetTemp.stream().sorted((x1, x2) -> {
            int num1 = Integer.parseInt(x1.get("finalPoint").toString());
            int num2 = Integer.parseInt(x2.get("finalPoint").toString());
            if (num1 > num2) {
                return -1;
            } else if (num1 < num2) {
                return 1;
            } else {
                return 0;
            }
        }).map(m -> {
            PostInfo postInfo = objectMapper.convertValue(m, PostInfo.class);
            postInfo.setUserLiked((boolean) m.get("isUserLiked"));
            return postInfo;
        }).distinct().collect(Collectors.toList());
    }
}
