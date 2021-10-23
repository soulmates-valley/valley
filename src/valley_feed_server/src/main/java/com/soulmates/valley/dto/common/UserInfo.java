package com.soulmates.valley.dto.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class UserInfo {

    private final Long userId;

    private final String nickname;

    private final String userEmail;
}
