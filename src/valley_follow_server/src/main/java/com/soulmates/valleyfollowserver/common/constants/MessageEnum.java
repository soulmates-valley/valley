package com.soulmates.valleyfollowserver.common.constants;

public enum MessageEnum {
    /* Exchange 이름 정의 */
    TOPIC_EXCHANGE_NAME("follow.topic"),
    /* Exchange 이름 정의 끝 */

    /* 라우팅 키 이름 정의 */
    FOLLOW_CREATE("follow.create");
    /* 라우팅키 이름 정의 끝 */

    private final String value;

    MessageEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
