package com.soulmates.valley.feature.posting.util;

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
public class PostMapper {
    private final ObjectMapper objectMapper;

    public List<PostInfo> convertPostList(Collection<Map<String, Object>> postList) {
        return postList.stream().map(m -> {
            PostInfo postInfo = objectMapper.convertValue(m, PostInfo.class);
            postInfo.setUserLiked((boolean) m.get("isUserLiked"));
            return postInfo;
        }).collect(Collectors.toList());
    }
}
