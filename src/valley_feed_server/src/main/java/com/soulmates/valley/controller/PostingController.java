package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.dto.Users;
import com.soulmates.valley.common.resolver.CurrentUser;
import com.soulmates.valley.feature.posting.dto.*;
import com.soulmates.valley.feature.posting.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/post")
@RestController
public class PostingController {

    private final PostService postService;

    @PostMapping(consumes = {"multipart/form-data"})
    public CommonResponse<Object> addPost(@CurrentUser Users users,
                                     @ModelAttribute @Valid PostAddRequest postUploadRequest) {
        postService.addPost(users.getUserId(), postUploadRequest);
        return new CommonResponse<>(ResponseCode.SUCCESS);
    }

    @GetMapping
    public CommonResponse<PostDetail> getPostDetail(@CurrentUser Users users,
                                           @RequestParam(required = true) Long postId) {
        PostDetail postDetail = postService.getPostDetail(postId, users.getUserId());
        return new CommonResponse<>(ResponseCode.SUCCESS, postDetail);
    }

    @GetMapping("/detail")
    public CommonResponse<List<PostDetail>> getUserPostList(@CurrentUser Users users,
                                             @Valid PostLimitRequest postLimitRequest) {
        List<PostDetail> postDetailList = postService.getUserPostList(users.getUserId(), postLimitRequest);
        return new CommonResponse<>(ResponseCode.SUCCESS, postDetailList);
    }
}
