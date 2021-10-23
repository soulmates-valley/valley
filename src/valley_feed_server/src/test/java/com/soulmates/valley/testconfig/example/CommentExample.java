package com.soulmates.valley.testconfig.example;

import com.soulmates.valley.common.dto.Users;
import com.soulmates.valley.feature.comment.dto.CommentInfo;

import java.time.LocalDateTime;

public class CommentExample {
    public CommentInfo example(Users users){
        return CommentInfo.builder()
                .id(12L)
                .content("content")
                .createDt(LocalDateTime.now().minusHours(10))
                .profileImg("url")
                .userId(users.getUserId())
                .nickname(users.getNickname()).build();
    }

    public CommentInfo example2(Users users){
        return CommentInfo.builder()
                .id(13L)
                .content("content2")
                .createDt(LocalDateTime.now().minusHours(9))
                .profileImg("url2")
                .userId(users.getUserId())
                .nickname(users.getNickname()).build();
    }
}
