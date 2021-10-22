package com.soulmates.valley.feature.feed.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulmates.valley.feature.posting.dto.PostInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FeedCombiner {

    private final ObjectMapper objectMapper;

    public List<PostInfo> combineFeed(Collection<Map<String, Object>> feedIntersetTemp, Collection<Map<String, Object>> feedFollowTemp) {
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
