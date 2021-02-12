package com.soulmates.valley.common.constants;

public enum CodeEnum {
    SUCCESS(200);

    private final int code;

    CodeEnum(int code) {
        this.code = code;
    }

    public int getValue() {
        return code;
    }
}
