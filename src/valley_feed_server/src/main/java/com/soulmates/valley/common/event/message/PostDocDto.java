package com.soulmates.valley.common.event.message;

import com.soulmates.valley.domain.model.doc.PostDoc;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class PostDocDto {
    private long postId;

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

    public static PostDocDto of(PostDoc postDoc) {
        return PostDocDto.builder()
                .postId(postDoc.getId())
                .content(postDoc.getContent())
                .code(postDoc.getCode())
                .codeType(postDoc.getCodeType())
                .link(postDoc.getLink())
                .linkContent(postDoc.getLinkContent())
                .linkTitle(postDoc.getLinkTitle())
                .linkImage(postDoc.getLinkImage())
                .linkSiteName(postDoc.getLinkSiteName())
                .userId(postDoc.getUserId())
                .userImage(postDoc.getUserImage())
                .hashTag(postDoc.getHashTag())
                .images(postDoc.getImages())
                .nickname(postDoc.getNickname())
                .createDt(LocalDateTime.now())
                .postType(postDoc.getPostType())
                .likeCnt(0)
                .commentCnt(0).build();
    }
}
