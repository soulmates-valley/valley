package com.soulmates.valley.common.constants;

public enum MessageEnum {
    /* Exchange 이름 정의 */
    TOPIC_EXCHANGE_NAME("feed.topic"),
    /* Exchange 이름 정의 끝 */

    /* 라우팅 키 이름 정의 */
    COMMENT_CREATE("comment.create"),
    LIKE_CREATE("like.create"),
    POST_CREATE("post.create"),
    HASHTAG_CREATE("hash.create");
    /* 라우팅키 이름 정의 끝 */

    private final String value;

    MessageEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
