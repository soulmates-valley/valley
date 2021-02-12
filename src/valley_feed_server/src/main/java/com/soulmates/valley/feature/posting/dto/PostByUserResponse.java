package com.soulmates.valley.feature.posting.dto;

import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class PostByUserResponse {
    private Long sincePostId;

    private Long maxPostId;

    private int size;

    private List<PostDetail> content;

    public static PostByUserResponse of(List<PostDetail> postInfoList) {
        if (postInfoList.size() == 0)
            return null;
        return PostByUserResponse.builder()
                .content(postInfoList)
                .size(postInfoList.size())
                .sincePostId(postInfoList.get(0).getPostId())
                .maxPostId(postInfoList.get(postInfoList.size() - 1).getPostId()).build();
    }
}
