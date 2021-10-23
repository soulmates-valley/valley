package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.CodeEnum;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.dto.Users;
import com.soulmates.valley.common.resolver.CurrentUser;
import com.soulmates.valley.feature.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/like")
@RestController
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<CommonResponse> addLikeToPost(@CurrentUser Users users,
                                           @RequestParam("postId") Long postId) {
        likeService.addLikeToPost(postId, users.getUserId());
        return ResponseEntity.ok().body(new CommonResponse(CodeEnum.SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse> deleteLikeToPost(@CurrentUser Users users,
                                              @RequestParam("postId") Long postId) {
        likeService.deleteLikeToPost(postId, users.getUserId());
        return ResponseEntity.ok().body(new CommonResponse(CodeEnum.SUCCESS));
    }
}
