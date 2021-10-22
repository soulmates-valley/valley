package com.soulmates.valley.common.exception;

import com.soulmates.valley.common.constants.ErrorEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    protected ErrorEnum errorEnum;

    public CustomException(ErrorEnum errorEnum) {
        super(errorEnum.toString());
        this.errorEnum = errorEnum;
    }
}