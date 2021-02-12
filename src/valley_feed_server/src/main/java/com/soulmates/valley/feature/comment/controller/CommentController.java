package com.soulmates.valley.feature.comment.controller;

import com.soulmates.valley.common.constants.CodeEnum;
import com.soulmates.valley.common.constants.ErrorEnum;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.util.JWTParser;
import com.soulmates.valley.feature.comment.dto.CommentAddRequest;
import com.soulmates.valley.feature.comment.dto.CommentInfo;
import com.soulmates.valley.feature.comment.dto.CommentPageLimitReuqest;
import com.soulmates.valley.feature.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comment")
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> addCommentToPost(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
                                              @RequestBody @Valid CommentAddRequest commentAddRequest, Errors error) {
        if (error.hasErrors()) {
            String errMsg = Objects.requireNonNull(error.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
        }
        Long userId = JWTParser.getUidFromJWT(token);

        CommentInfo commentInfo = commentService.addCommentToPost(commentAddRequest, userId);

        return ResponseEntity.ok().body(new CommonResponse(CodeEnum.SUCCESS, commentInfo));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getCommentFromPost(@RequestParam(required = true) Long postId,
                                                @Valid CommentPageLimitReuqest commentPageLimitReuqest, Errors error) {
        if (error.hasErrors()) {
            String errMsg = Objects.requireNonNull(error.getFieldError()).getDefaultMessage();
            log.info(errMsg);
            return ResponseEntity.ok(new CommonResponse(ErrorEnum.PARAM_INVALID, errMsg));
        }

        List<CommentInfo> commentSlice = commentService.getCommentFromPost(postId,
                commentPageLimitReuqest.getPage(),
                commentPageLimitReuqest.getSize());
        return ResponseEntity.ok().body(new CommonResponse(CodeEnum.SUCCESS, commentSlice));
    }
}


