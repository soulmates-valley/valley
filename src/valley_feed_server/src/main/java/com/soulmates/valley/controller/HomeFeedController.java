package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.CodeEnum;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.util.JWTParser;
import com.soulmates.valley.feature.feed.dto.FeedLimitRequest;
import com.soulmates.valley.feature.feed.service.HomeFeedService;
import com.soulmates.valley.feature.posting.dto.PostDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RequestMapping("/feed")
@RestController
public class HomeFeedController {

    private final HomeFeedService homeFeedService;

    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getFeedPostAll(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
                                            @Valid FeedLimitRequest feedLimitRequest, Errors error) {
        Long userId = JWTParser.getUidFromJWT(token);

        List<PostDetail> postInfoList = homeFeedService.getFeedPostList(userId, feedLimitRequest.getPage(), feedLimitRequest.getSize());
        return ResponseEntity.ok(new CommonResponse(CodeEnum.SUCCESS, postInfoList));
    }
}
