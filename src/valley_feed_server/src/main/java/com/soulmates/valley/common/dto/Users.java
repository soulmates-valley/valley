package com.soulmates.valley.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class Users {

    private final Long userId;

    private final String nickname;

    private final String userEmail;
}
