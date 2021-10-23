package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.CodeEnum;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.dto.Users;
import com.soulmates.valley.common.resolver.CurrentUser;
import com.soulmates.valley.feature.feed.service.RecommendFeedService;
import com.soulmates.valley.feature.posting.dto.PostDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CommonResponse> getRecommendFeed(@CurrentUser Users users) {
        List<PostDetail> feed = recommendFeedService.getRecommendFeedByInterest(users.getUserId());
        return ResponseEntity.ok(new CommonResponse(CodeEnum.SUCCESS, feed));
    }
}
