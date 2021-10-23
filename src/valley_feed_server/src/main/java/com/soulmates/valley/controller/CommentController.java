package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.dto.common.CommonResponse;
import com.soulmates.valley.dto.common.UserInfo;
import com.soulmates.valley.common.resolver.CurrentUser;
import com.soulmates.valley.dto.comment.CommentAddRequest;
import com.soulmates.valley.dto.comment.CommentInfo;
import com.soulmates.valley.dto.comment.CommentPageLimitReuqest;
import com.soulmates.valley.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comment")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommonResponse<CommentInfo> addCommentToPost(@CurrentUser UserInfo userInfo,
                                                        @RequestBody @Valid CommentAddRequest commentAddRequest) {
        CommentInfo commentInfo = commentService.addCommentToPost(commentAddRequest, userInfo.getUserId());
        return new CommonResponse<>(ResponseCode.SUCCESS, commentInfo);
    }

    @GetMapping("/all")
    public CommonResponse<List<CommentInfo>> getCommentFromPost(@RequestParam(required = true) Long postId,
                                                                @Valid CommentPageLimitReuqest commentPageLimitReuqest) {
        List<CommentInfo> commentSlice = commentService.getCommentFromPost(postId,
                commentPageLimitReuqest.getPage(),
                commentPageLimitReuqest.getSize());
        return new CommonResponse<>(ResponseCode.SUCCESS, commentSlice);
    }
}


