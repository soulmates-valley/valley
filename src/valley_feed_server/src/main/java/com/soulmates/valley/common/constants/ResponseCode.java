package com.soulmates.valley.common.constants;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS(200, "success"),

    /* 기타 오류 메세지 정의 */
    ETC(1000, "알 수 없는 오류입니다."),
    PARAM_INVALID(1001, "올바르지 않은 파라미터입니다."),
    UPLOAD_ERROR(1002, "파일 업로드 오류입니다."),
    RABBIT_CONNECT_ERROR(1003, "Rabbitmq 통신 오류입니다."),
    /* 기타 오류 메세지 정의 끝*/

    /* 회원 오류 메세지 정의 */
    USER_NOT_FOUND(1100, "해당 유저가 존재하지 않습니다."),
    /* 회원 오류 메세지 정의 끝*/

    /* 포스팅 오류 메세지 정의 */
    POST_NOT_FOUND(1200, "해당 게시글이 존재하지 않습니다."),
    POST_NOT_AUTH(1201, "해당 게시글에 대한 권한이 없습니다."),
    POST_OWNER_NOT_FOUND(1202, "해당 게시글의 글쓴이를 찾을 수 없습니다."),
    /* 포스팅 오류 메세지 정의 끝*/

    /* 피드 오류 메시지 정의 */
    FEED_NOT_FOUND(1400, "해당 회원의 피드 게시글이 존재하지않습니다."),
    /* 피드 오류 메시지 정의 끝*/

    /* 댓글 오류 메세지 정의 */
    COMMENT_NOT_CREATE(1300, "댓글 생성에 실패했습니다."),
    /* 댓글 오류 메세지 정의 끝*/

    /* 좋아요 오류 메세지 정의 */
    LIKE_UNDER_ZERO(1500, "게시물의 총 좋아요 개수가 0 이하로 변경될 수 없습니다.");
    /* 좋아요 오류 메세지 정의 끝*/

    private final int code;

    private final String message;

    ResponseCode(int code, String message){
        this.code = code;
        this.message = message;
    }
}