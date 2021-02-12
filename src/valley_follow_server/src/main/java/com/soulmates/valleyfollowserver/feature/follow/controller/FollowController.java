package com.soulmates.valleyfollowserver.feature.follow.controller;

import com.soulmates.valleyfollowserver.common.constants.CodeEnum;
import com.soulmates.valleyfollowserver.common.dto.CommonResponse;
import com.soulmates.valleyfollowserver.common.util.JWTParser;
import com.soulmates.valleyfollowserver.feature.follow.dto.FollowCount;
import com.soulmates.valleyfollowserver.feature.follow.dto.FollowInfo;
import com.soulmates.valleyfollowserver.feature.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/follow")
@RestController
public class FollowController {
    private final FollowService followService;

    @PostMapping("/{toUserId}")
    public ResponseEntity<?> addFollow(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
                                       @PathVariable(required = true) Long toUserId) {
        Long userId = JWTParser.getUidFromJWT(token);

        followService.addFollow(userId, toUserId);
        return ResponseEntity.ok().body(new CommonResponse(CodeEnum.SUCCESS));
    }

    @DeleteMapping("/{toUserId}")
    public ResponseEntity<?> deleteFollow(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
                                          @PathVariable(required = true) Long toUserId) {
        Long userId = JWTParser.getUidFromJWT(token);

        followService.deleteFollow(userId, toUserId);
        return ResponseEntity.ok().body(new CommonResponse(CodeEnum.SUCCESS));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getFollowCnt(@RequestParam(required = true) Long userId) {
        FollowCount followCount = followService.getFollowCnt(userId);
        return ResponseEntity.ok().body(new CommonResponse(CodeEnum.SUCCESS, followCount));
    }

    @GetMapping("/in")
    public ResponseEntity<?> getFollowerList(@RequestParam(required = true) Long userId,
                                             @RequestParam(required = true) int page,
                                             @RequestParam(required = true) int size) {
        List<FollowInfo> followCount = followService.getFollowerList(userId, page, size);
        return ResponseEntity.ok().body(new CommonResponse(CodeEnum.SUCCESS, followCount));
    }

    @GetMapping("/out")
    public ResponseEntity<?> getFollowedList(@RequestParam(required = true) Long userId,
                                             @RequestParam(required = true) int page,
                                             @RequestParam(required = true) int size) {
        List<FollowInfo> followCount = followService.getFollowedList(userId, page, size);
        return ResponseEntity.ok().body(new CommonResponse(CodeEnum.SUCCESS, followCount));
    }

    @PostMapping("/TEST/{fromUserId}/{toUserId}")
    public ResponseEntity<?> testaddFollow(@PathVariable(required = true) Long fromUserId,
                                           @PathVariable(required = true) Long toUserId) {

        followService.addFollow(fromUserId, toUserId);
        return ResponseEntity.ok().body(new CommonResponse(CodeEnum.SUCCESS));
    }
}
