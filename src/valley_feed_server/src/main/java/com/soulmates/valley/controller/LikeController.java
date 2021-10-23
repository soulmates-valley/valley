package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.dto.Users;
import com.soulmates.valley.common.resolver.CurrentUser;
import com.soulmates.valley.feature.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/like")
@RestController
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public CommonResponse<Object> addLikeToPost(@CurrentUser Users users,
                                           @RequestParam("postId") Long postId) {
        likeService.addLikeToPost(postId, users.getUserId());
        return new CommonResponse<>(ResponseCode.SUCCESS);
    }

    @DeleteMapping
    public CommonResponse<Object> deleteLikeToPost(@CurrentUser Users users,
                                              @RequestParam("postId") Long postId) {
        likeService.deleteLikeToPost(postId, users.getUserId());
        return new CommonResponse<>(ResponseCode.SUCCESS);
    }
}
