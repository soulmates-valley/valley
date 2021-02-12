package com.soulmates.valley.feature.feed.service;

import com.soulmates.valley.common.constants.ErrorEnum;
import com.soulmates.valley.common.exception.CustomException;
import com.soulmates.valley.feature.posting.util.PostMapper;
import com.soulmates.valley.domain.model.doc.PostDoc;
import com.soulmates.valley.domain.repository.doc.PostDocRepository;
import com.soulmates.valley.domain.repository.graph.TimeLineGraphRepository;
import com.soulmates.valley.feature.posting.dto.PostDetail;
import com.soulmates.valley.feature.posting.dto.PostInfo;
import com.soulmates.valley.feature.posting.util.PostCombiner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HomeFeedService {
    private final TimeLineGraphRepository timeLineGraphRepository;
    private final PostDocRepository postDocRepository;
    private final RedisTemplate<String, PostInfo> redisTemplate;
    private final PostMapper postMapper;

    private static final int FEED_LENGTH = 150;
    private static final int FEED_FROM_DATE = 5;

    public List<PostDetail> getFeedPostList(Long userId, long page, long size) {
        final String FEED_KEY = "feed:" + userId;

        // post info caching when first request or reload
        if (page == 0) {
            List<PostInfo> postInitList = getPostInfoAll(userId);
            if (postInitList == null || postInitList.isEmpty())
                return Collections.emptyList();
            redisTemplate.delete(FEED_KEY);
            redisTemplate.opsForList().rightPushAll(FEED_KEY, postInitList);
        }

        List<PostInfo> postInfoList = redisTemplate.opsForList().range(FEED_KEY, page * size, page * size + size - 1);
        if (postInfoList == null || postInfoList.isEmpty())
            return Collections.emptyList();

        List<Long> postIdList = postInfoList.stream().map(PostInfo::getPostId).collect(Collectors.toList());
        List<PostDoc> postDocList = postDocRepository.findAllByIdIn(postIdList);
        return PostCombiner.combinePostList(postDocList, postInfoList);
    }

    public List<PostInfo> getPostInfoAll(Long userId) {
        LocalDateTime fromTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(FEED_FROM_DATE);
        Collection<Map<String, Object>> postIdList = timeLineGraphRepository.getFeedPostInfo(userId, FEED_LENGTH, fromTime);
        return postMapper.convertPostList(postIdList);
    }
}
