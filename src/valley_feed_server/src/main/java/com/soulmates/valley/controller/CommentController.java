package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.dto.Users;
import com.soulmates.valley.common.resolver.CurrentUser;
import com.soulmates.valley.feature.comment.dto.CommentAddRequest;
import com.soulmates.valley.feature.comment.dto.CommentInfo;
import com.soulmates.valley.feature.comment.dto.CommentPageLimitReuqest;
import com.soulmates.valley.feature.comment.service.CommentService;
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
    public CommonResponse<CommentInfo> addCommentToPost(@CurrentUser Users users,
                                              @RequestBody @Valid CommentAddRequest commentAddRequest) {
        CommentInfo commentInfo = commentService.addCommentToPost(commentAddRequest, users.getUserId());
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


