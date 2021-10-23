package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.dto.UserInfo;
import com.soulmates.valley.common.resolver.CurrentUser;
import com.soulmates.valley.dto.posting.PostAddRequest;
import com.soulmates.valley.dto.posting.PostDetail;
import com.soulmates.valley.dto.posting.PostLimitRequest;
import com.soulmates.valley.service.PostService;
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
    public CommonResponse<Object> addPost(@CurrentUser UserInfo userInfo,
                                     @ModelAttribute @Valid PostAddRequest postUploadRequest) {
        postService.addPost(userInfo.getUserId(), postUploadRequest);
        return new CommonResponse<>(ResponseCode.SUCCESS);
    }

    @GetMapping
    public CommonResponse<PostDetail> getPostDetail(@CurrentUser UserInfo userInfo,
                                                    @RequestParam(required = true) Long postId) {
        PostDetail postDetail = postService.getPostDetail(postId, userInfo.getUserId());
        return new CommonResponse<>(ResponseCode.SUCCESS, postDetail);
    }

    @GetMapping("/detail")
    public CommonResponse<List<PostDetail>> getUserPostList(@CurrentUser UserInfo userInfo,
                                             @Valid PostLimitRequest postLimitRequest) {
        List<PostDetail> postDetailList = postService.getUserPostList(userInfo.getUserId(), postLimitRequest);
        return new CommonResponse<>(ResponseCode.SUCCESS, postDetailList);
    }
}
