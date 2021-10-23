package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.dto.UserInfo;
import com.soulmates.valley.common.resolver.CurrentUser;
import com.soulmates.valley.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/like")
@RestController
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public CommonResponse<Object> addLikeToPost(@CurrentUser UserInfo userInfo,
                                           @RequestParam("postId") Long postId) {
        likeService.addLikeToPost(postId, userInfo.getUserId());
        return new CommonResponse<>(ResponseCode.SUCCESS);
    }

    @DeleteMapping
    public CommonResponse<Object> deleteLikeToPost(@CurrentUser UserInfo userInfo,
                                              @RequestParam("postId") Long postId) {
        likeService.deleteLikeToPost(postId, userInfo.getUserId());
        return new CommonResponse<>(ResponseCode.SUCCESS);
    }
}
