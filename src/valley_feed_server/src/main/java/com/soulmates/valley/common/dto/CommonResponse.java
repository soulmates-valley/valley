package com.soulmates.valley.common.dto;

import com.soulmates.valley.common.constants.CodeEnum;
import com.soulmates.valley.common.constants.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommonResponse {
    private int code;
    Object data;

    public CommonResponse(CodeEnum code) {
        this.code = code.getValue();
        this.data = "";
    }

    public CommonResponse(CodeEnum code, Object data) {
        this.code = code.getValue();
        this.data = data;
    }

    public CommonResponse(ErrorEnum errorEnum) {
        this.code = errorEnum.getErrCode();
        this.data = errorEnum.getMessage();
    }

    public CommonResponse(ErrorEnum errorEnum, String errMsg) {
        this.code = errorEnum.getErrCode();
        this.data = errMsg;
    }
}
