package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.CodeEnum;
import com.soulmates.valley.common.constants.ErrorEnum;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.exception.CustomException;
import com.soulmates.valley.common.util.JWTParser;
import com.soulmates.valley.feature.posting.dto.*;
import com.soulmates.valley.feature.posting.service.PostService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RequestMapping("/post")
@RestController
public class PostingController {
    private final PostService postService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<CommonResponse> addPost(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
                                     @ModelAttribute @Valid PostAddRequest postUploadRequest, Errors error) {
        Long userId = JWTParser.getUidFromJWT(token);

        postService.addPost(userId, postUploadRequest);
        return ResponseEntity.ok(new CommonResponse(CodeEnum.SUCCESS));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getPostDetail(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
                                           @RequestParam(required = true) Long postId) {
        Long userId = JWTParser.getUidFromJWT(token);

        PostDetail postDetail = postService.getPostDetail(postId, userId);
        return ResponseEntity.ok(new CommonResponse(CodeEnum.SUCCESS, postDetail));
    }

    @GetMapping("/detail")
    public ResponseEntity<CommonResponse> getUserPostList(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
                                             @Valid PostLimitRequest postLimitRequest, Errors error) {
        Long userId = JWTParser.getUidFromJWT(token);

        List<PostDetail> postDetailList = postService.getUserPostList(userId, postLimitRequest);

        return ResponseEntity.ok(new CommonResponse(CodeEnum.SUCCESS, postDetailList));
    }
}
