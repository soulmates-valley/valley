package com.soulmates.valley.service;

import com.soulmates.valley.domain.repository.TimeLineGraphRepository;
import com.soulmates.valley.dto.posting.PostDetail;
import com.soulmates.valley.dto.posting.PostInfo;
import com.soulmates.valley.common.util.post.PostConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HomeFeedService {

    private final TimeLineGraphRepository timeLineGraphRepository;
    private final RedisTemplate<String, PostInfo> redisTemplate;
    private final PostConverter postConverter;

    private static final int FEED_LENGTH = 150;
    private static final int FEED_FROM_DATE = 5;

    /**
     * 홈피드 조회
     * (게시글 중복 조회 문제 발생에 대한 차선책 코드. 향후 개선 필요.)
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
            List<PostInfo> postInitList = getFeedPostInfo(userId);
            if (postInitList == null || postInitList.isEmpty())
                return Collections.emptyList();
            redisTemplate.delete(FEED_KEY);
            redisTemplate.opsForList().rightPushAll(FEED_KEY, postInitList);
        }

        List<PostInfo> postInfoList = redisTemplate.opsForList().range(FEED_KEY, page * size, page * size + size - 1);
        if (postInfoList == null || postInfoList.isEmpty())
            return Collections.emptyList();

        return postConverter.convertToPost(postInfoList);
    }

    private List<PostInfo> getFeedPostInfo(Long userId) {
        LocalDateTime fromTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(FEED_FROM_DATE);
        return timeLineGraphRepository.getFeedPostInfo(userId, FEED_LENGTH, fromTime);
    }
}
