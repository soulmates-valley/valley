package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.dto.Users;
import com.soulmates.valley.common.resolver.CurrentUser;
import com.soulmates.valley.feature.feed.dto.FeedLimitRequest;
import com.soulmates.valley.feature.feed.service.HomeFeedService;
import com.soulmates.valley.feature.posting.dto.PostDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/feed")
@RestController
public class HomeFeedController {

    private final HomeFeedService homeFeedService;

    @GetMapping("/all")
    public CommonResponse<List<PostDetail>> getFeedPostAll(@CurrentUser Users users,
                                            @Valid FeedLimitRequest feedLimitRequest) {
        List<PostDetail> postInfoList = homeFeedService.getFeedPostList(users.getUserId(), feedLimitRequest.getPage(), feedLimitRequest.getSize());
        return new CommonResponse<>(ResponseCode.SUCCESS, postInfoList);
    }
}
