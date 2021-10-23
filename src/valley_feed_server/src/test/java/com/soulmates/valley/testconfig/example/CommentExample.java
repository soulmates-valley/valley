package com.soulmates.valley.testconfig.example;

import com.soulmates.valley.common.dto.UserInfo;
import com.soulmates.valley.dto.comment.CommentInfo;

import java.time.LocalDateTime;

public class CommentExample {
    public CommentInfo example(UserInfo userInfo){
        return CommentInfo.builder()
                .id(12L)
                .content("content")
                .createDt(LocalDateTime.now().minusHours(10))
                .profileImg("url")
                .userId(userInfo.getUserId())
                .nickname(userInfo.getNickname()).build();
    }

    public CommentInfo example2(UserInfo userInfo){
        return CommentInfo.builder()
                .id(13L)
                .content("content2")
                .createDt(LocalDateTime.now().minusHours(9))
                .profileImg("url2")
                .userId(userInfo.getUserId())
                .nickname(userInfo.getNickname()).build();
    }
}
