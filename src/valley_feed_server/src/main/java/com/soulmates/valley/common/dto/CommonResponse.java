package com.soulmates.valley.common.dto;

import com.soulmates.valley.common.constants.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommonResponse<T> {
    private int code;
    private String message;
    T data;

    public CommonResponse(ResponseCode responseCode) {
        this.code = responseCode.getErrCode();
        this.message = responseCode.getMessage();
    }

    public CommonResponse(ResponseCode responseCode, T data) {
        this.code = responseCode.getErrCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }
}
