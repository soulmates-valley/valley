package com.soulmates.valley.feature.feed.controller;

import com.soulmates.valley.common.constants.CodeEnum;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.util.JWTParser;
import com.soulmates.valley.feature.feed.service.RecommendFeedService;
import com.soulmates.valley.feature.posting.dto.PostDetail;
import com.soulmates.valley.feature.posting.dto.PostInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/recommend/feed")
@RestController
public class RecommendFeedController {
    private final RecommendFeedService recommendFeedService;

    @GetMapping
    public ResponseEntity<?> getRecommendFeed(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long userId = JWTParser.getUidFromJWT(token);

        List<PostDetail> feed = recommendFeedService.getRecommendFeedByInterest(userId);
        return ResponseEntity.ok().body(new CommonResponse(CodeEnum.SUCCESS, feed));
    }
}
