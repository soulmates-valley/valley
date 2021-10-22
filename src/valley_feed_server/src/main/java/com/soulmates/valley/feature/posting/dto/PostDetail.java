package com.soulmates.valley.feature.posting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soulmates.valley.domain.model.doc.PostDoc;
import lombok.*;
import org.json.JSONException;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class PostDetail {

    private Long postId;

    private String postType;

    private String content;

    private List<String> images;

    private String code;

    private String codeType;

    private String link;

    private String linkTitle;

    private String linkImage;

    private String linkContent;

    private String linkSiteName;

    private int likeCnt;

    private int commentCnt;

    private Long userId;

    private String userImage;

    private String nickname;

    private List<String> hashTag;

    private LocalDateTime createDt;

    @JsonProperty("isUserLiked")
    private boolean isUserLiked;

    public static PostDetail of(PostDoc post, boolean isUserLiked) {
        return PostDetail.builder()
                .postId(post.getId())
                .postType(post.getPostType())
                .code(post.getCode())
                .codeType(post.getCodeType())
                .commentCnt(post.getCommentCnt())
                .content(post.getContent())
                .images(post.getImages())
                .nickname(post.getNickname())
                .userId(post.getUserId())
                .userImage(post.getUserImage())
                .likeCnt(post.getLikeCnt())
                .commentCnt(post.getCommentCnt())
                .hashTag(post.getHashTag())
                .link(post.getLink())
                .linkContent(post.getLinkContent())
                .linkImage(post.getLinkImage())
                .linkSiteName(post.getLinkSiteName())
                .linkTitle(post.getLinkTitle())
                .isUserLiked(isUserLiked)
                .createDt(post.getCreateDt()).build();
    }
}
