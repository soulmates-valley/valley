package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.dto.UserInfo;
import com.soulmates.valley.common.resolver.CurrentUser;
import com.soulmates.valley.service.RecommendFeedService;
import com.soulmates.valley.dto.posting.PostDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/recommend/feed")
@RestController
public class RecommendFeedController {

    private final RecommendFeedService recommendFeedService;

    @GetMapping
    public CommonResponse<List<PostDetail>> getRecommendFeed(@CurrentUser UserInfo userInfo) {
        List<PostDetail> feed = recommendFeedService.getRecommendFeedByInterest(userInfo.getUserId());
        return new CommonResponse<>(ResponseCode.SUCCESS, feed);
    }
}
