package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.dto.common.CommonResponse;
import com.soulmates.valley.dto.common.UserInfo;
import com.soulmates.valley.common.resolver.CurrentUser;
import com.soulmates.valley.dto.feed.FeedLimitRequest;
import com.soulmates.valley.service.HomeFeedService;
import com.soulmates.valley.dto.posting.PostDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/feed")
@RestController
public class HomeFeedController {

    private final HomeFeedService homeFeedService;

    @GetMapping("/all")
    public CommonResponse<List<PostDetail>> getFeedPostAll(@CurrentUser UserInfo userInfo,
                                            @Valid FeedLimitRequest feedLimitRequest) {
        List<PostDetail> postInfoList = homeFeedService.getFeedPostList(userInfo.getUserId(), feedLimitRequest.getPage(), feedLimitRequest.getSize());
        return new CommonResponse<>(ResponseCode.SUCCESS, postInfoList);
    }
}
