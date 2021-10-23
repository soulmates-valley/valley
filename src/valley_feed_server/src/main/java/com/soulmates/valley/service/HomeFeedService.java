package com.soulmates.valley.service;

import com.soulmates.valley.common.util.PostMapper;
import com.soulmates.valley.domain.model.PostDoc;
import com.soulmates.valley.domain.repository.PostDocRepository;
import com.soulmates.valley.domain.repository.TimeLineGraphRepository;
import com.soulmates.valley.dto.posting.PostDetail;
import com.soulmates.valley.dto.posting.PostInfo;
import com.soulmates.valley.common.util.PostCombiner;
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

    /**
     * 홈피드 조회
     *
     * @param userId 조회하고자 하는 user 식별자
     * @param page 조회 page
     * @param size 조회 size
     * @return 홈피드 게시글 List
     */
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

    private List<PostInfo> getPostInfoAll(Long userId) {
        LocalDateTime fromTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(FEED_FROM_DATE);
        Collection<Map<String, Object>> postIdList = timeLineGraphRepository.getFeedPostInfo(userId, FEED_LENGTH, fromTime);
        return postMapper.convertPostList(postIdList);
    }
}
