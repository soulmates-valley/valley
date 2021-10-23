package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.dto.common.CommonResponse;
import com.soulmates.valley.dto.common.UserInfo;
import com.soulmates.valley.common.resolver.CurrentUser;
import com.soulmates.valley.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/like")
@RestController
public class LikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    public CommonResponse<Object> addLikeToPost(@CurrentUser UserInfo userInfo,
                                           @RequestParam("postId") Long postId) {
        postLikeService.addLikeToPost(postId, userInfo.getUserId());
        return new CommonResponse<>(ResponseCode.SUCCESS);
    }

    @DeleteMapping
    public CommonResponse<Object> deleteLikeToPost(@CurrentUser UserInfo userInfo,
                                              @RequestParam("postId") Long postId) {
        postLikeService.deleteLikeToPost(postId, userInfo.getUserId());
        return new CommonResponse<>(ResponseCode.SUCCESS);
    }
}
