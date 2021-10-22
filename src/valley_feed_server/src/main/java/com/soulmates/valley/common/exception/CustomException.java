package com.soulmates.valley.common.exception;

import com.soulmates.valley.common.constants.ErrorEnum;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorEnum errorEnum;

    public CustomException(ErrorEnum errorEnum) {
        super(errorEnum.toString());
        this.errorEnum = errorEnum;
    }
}