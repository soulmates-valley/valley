package com.soulmates.valley.testconfig.example;

import com.soulmates.valley.dto.common.UserInfo;
import com.soulmates.valley.dto.posting.PostDetail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostEaxample {
    public PostDetail example(UserInfo user){
        List<String> images = new ArrayList<String>();
        images.add("url");
        List<String> hashTag = new ArrayList<String>();
        hashTag.add("hash");

        return PostDetail.builder()
                .postId(111L)
                .postType("link")
                .code("code")
                .codeType("100")
                .commentCnt(56)
                .content("content")
                .images(images)
                .nickname(user.getNickname())
                .userId(user.getUserId())
                .userImage("hihi")
                .likeCnt(3)
                .commentCnt(5)
                .hashTag(hashTag)
                .link("link")
                .linkContent("linkContent")
                .linkImage("image")
                .linkSiteName("siteName")
                .linkTitle("title")
                .isUserLiked(true)
                .createDt(LocalDateTime.now().minusDays(1)).build();
    }

    public PostDetail example2(UserInfo user){
        List<String> images = new ArrayList<String>();
        images.add("url");
        List<String> hashTag = new ArrayList<String>();
        hashTag.add("hash");

        return PostDetail.builder()
                .postId(112L)
                .postType("link")
                .code("code2")
                .codeType("1002")
                .commentCnt(5)
                .content("content2")
                .images(images)
                .nickname(user.getNickname())
                .userId(user.getUserId())
                .userImage("hihi2")
                .likeCnt(31)
                .commentCnt(51)
                .hashTag(hashTag)
                .link("link2")
                .linkContent("linkContent2")
                .linkImage("image2")
                .linkSiteName("siteName2")
                .linkTitle("title2")
                .isUserLiked(false)
                .createDt(LocalDateTime.now().minusHours(6)).build();
    }
}
