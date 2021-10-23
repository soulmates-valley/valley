package com.soulmates.valley.common.exception;

import com.soulmates.valley.common.constants.ResponseCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ResponseCode responseCode;

    public CustomException(ResponseCode responseCode) {
        super(responseCode.toString());
        this.responseCode = responseCode;
    }
}