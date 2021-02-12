package com.soulmates.valleyfollowserver.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorEnum {
    /* 팔로우 오류 메시지 정의 */
    USER_NOT_FOUND(2100, "유저를 찾을 수 없습니다."),
    FOLLOW_ALREADY(2101, "이미 팔로우 상태입니다."),
    FOLLOW_NOT_FOUND(2102, "팔로우 상태가 아닙니다."),
    FOLLOW_SAME_USER(2102, "같은 유저에게 팔로우를 할수 없습니다."),
    /* 팔로우 오류 메시지 정의 끝*/

    /* 기타 오류 메세지 정의 */
    ETC(2000, "알 수 없는 오류입니다."),
    PARAM_INVALID(2001, "올바르지 않은 파라미터입니다.");
    /* 기타 오류 메세지 정의 끝*/

    private final ErrorResponse errorResponse;

    public String getMessage() {
        return this.errorResponse.getMessage();
    }

    public int getErrCode() {
        return this.errorResponse.getErrCode();
    }


    ErrorEnum(int errCode, String message) {
        this.errorResponse = new ErrorResponse(errCode, message);
    }

    @Getter
    public static class ErrorResponse {
        private final int errCode;
        private final String message;

        public ErrorResponse(int errCode, String message) {
            this.errCode = errCode;
            this.message = message;
        }
    }
}