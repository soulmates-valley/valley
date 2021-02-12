package com.soulmates.valley.feature.like.controller;

import com.soulmates.valley.common.constants.CodeEnum;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.util.JWTParser;
import com.soulmates.valley.feature.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RequiredArgsConstructor
@RequestMapping("/like")
@RestController
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<?> addLikeToPost(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
                                           @RequestParam("postId") Long postId) {
        Long userId = JWTParser.getUidFromJWT(token);

        likeService.addLikeToPost(postId, userId);
        return ResponseEntity.ok().body(new CommonResponse(CodeEnum.SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteLikeToPost(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
                                              @RequestParam("postId") Long postId) {
        Long userId = JWTParser.getUidFromJWT(token);

        likeService.deleteLikeToPost(postId, userId);
        return ResponseEntity.ok().body(new CommonResponse(CodeEnum.SUCCESS));
    }
}
