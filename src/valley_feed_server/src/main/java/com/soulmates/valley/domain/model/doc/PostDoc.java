package com.soulmates.valley.domain.model.doc;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.soulmates.valley.domain.constants.PostType;
import com.soulmates.valley.domain.model.graph.UserNode;
import com.soulmates.valley.feature.posting.dto.PostAddRequest;
import com.soulmates.valley.feature.posting.dto.PostUpdateRequest;
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
                .hashTag(postUploadRequest.getHashTag().stream().map((h)->{return h.replaceAll("\"","");}).collect(Collectors.toList()))
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

    public void updatePost(PostUpdateRequest postUpdateRequest) {
        this.content = postUpdateRequest.getContent();
        this.link = postUpdateRequest.getLink();
        this.code = postUpdateRequest.getCode();
        this.codeType = postUpdateRequest.getCodeType();

        if (postUpdateRequest.getLink() != null && !postUpdateRequest.getLink().isBlank()) {
            this.postType = PostType.LINK.getValue();
        } else if (postUpdateRequest.getCode() != null && !postUpdateRequest.getCode().isBlank()) {
            this.postType = PostType.CODE.getValue();
        } else {
            this.postType = PostType.TEXT.getValue();
        }
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
