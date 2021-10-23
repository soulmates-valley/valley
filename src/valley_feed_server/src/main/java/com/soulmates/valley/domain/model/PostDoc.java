package com.soulmates.valley.domain.model;


import com.soulmates.valley.domain.constants.PostType;
import com.soulmates.valley.dto.posting.PostAddRequest;
import com.soulmates.valley.dto.posting.PostUpdateRequest;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
@Builder
@Document("post")
public class PostDoc {

    @Id
    private long id;

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

    @CreatedDate
    private LocalDateTime createDt;

    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";

    public static PostDoc of(PostAddRequest postUploadRequest, List<String> images, UserNode userNode){
        PostDoc post = PostDoc.builder()
                .content(postUploadRequest.getContent())
                .code(postUploadRequest.getCode())
                .codeType(postUploadRequest.getCodeType())
                .link(postUploadRequest.getLink())
                .linkContent(postUploadRequest.getLinkContent())
                .linkTitle(postUploadRequest.getLinkTitle())
                .linkImage(postUploadRequest.getLinkImage())
                .linkSiteName(postUploadRequest.getLinkSiteName())
                .userId(userNode.getUserId())
                .userImage(userNode.getProfileImg())
                .hashTag(postUploadRequest.getHashTag().stream().map(PostDoc::apply).collect(Collectors.toList()))
                .images(images)
                .nickname(userNode.getNickname())
                .createDt(LocalDateTime.now())
                .likeCnt(0)
                .commentCnt(0).build();

        if (postUploadRequest.isExistImages()) {
            post.setPostType(PostType.IMAGE.getValue());
        } else if (postUploadRequest.getLink() != null && !postUploadRequest.getLink().isBlank()) {
            post.setPostType(PostType.LINK.getValue());
        } else if (postUploadRequest.getCode() != null && !postUploadRequest.getCode().isBlank()) {
            post.setPostType(PostType.CODE.getValue());
        } else {
            post.setPostType(PostType.TEXT.getValue());
        }

        return post;
    }

    private static String apply(String h) {
        return h.replaceAll("\"", "");
    }

    public boolean isExistHashTag(){
        return !this.hashTag.isEmpty();
    }

    public List<Map<String, String>> getHashTagList(){
        List<Map<String, String>> hashTagList = hashTag.stream()
                .map(h->{
                    Map<String,String> hash = new HashMap<>();
                    hash.put("hashTag",h);
                    return hash;
                }).collect(Collectors.toList());
        return hashTagList;
    }


    public void increaseLikeCnt() {
        this.likeCnt++;
    }

    public void decreaseLikeCnt() {
        this.likeCnt--;
    }

    public void increaseCommentCnt() {
        this.commentCnt++;
    }

    public void decreaseCommentCnt() {
        this.commentCnt--;
    }
}
