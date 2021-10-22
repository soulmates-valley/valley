package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.CodeEnum;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.util.JWTParser;
import com.soulmates.valley.feature.comment.dto.CommentAddRequest;
import com.soulmates.valley.feature.comment.dto.CommentInfo;
import com.soulmates.valley.feature.comment.dto.CommentPageLimitReuqest;
import com.soulmates.valley.feature.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CommonResponse> addCommentToPost(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
                                              @RequestBody @Valid CommentAddRequest commentAddRequest) {
        Long userId = JWTParser.getUidFromJWT(token);

        CommentInfo commentInfo = commentService.addCommentToPost(commentAddRequest, userId);

        return ResponseEntity.ok(new CommonResponse(CodeEnum.SUCCESS, commentInfo));
    }

    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getCommentFromPost(@RequestParam(required = true) Long postId,
                                                @Valid CommentPageLimitReuqest commentPageLimitReuqest) {
        List<CommentInfo> commentSlice = commentService.getCommentFromPost(postId,
                commentPageLimitReuqest.getPage(),
                commentPageLimitReuqest.getSize());
        return ResponseEntity.ok(new CommonResponse(CodeEnum.SUCCESS, commentSlice));
    }
}


