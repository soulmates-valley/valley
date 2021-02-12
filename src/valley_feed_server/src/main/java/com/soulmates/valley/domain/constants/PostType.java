package com.soulmates.valley.domain.constants;

public enum PostType {
    IMAGE("image"),
    LINK("link"),
    CODE("code"),
    TEXT("text");

    private final String value;

    PostType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
