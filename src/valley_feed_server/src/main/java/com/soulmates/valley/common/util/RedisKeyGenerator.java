package com.soulmates.valley.common.util;

public class RedisKeyGenerator {

    private RedisKeyGenerator(){}

    /**
     * Redis 전용 key 생성
     *
     * @param path 기본 path
     * @param seperator 구분자
     * @param keyList key List (매개변수 길이 제한 없음)
     * @return Redis 전용 key
     */
    public static String generateKey(String path, String seperator, Object... keyList){
        StringBuilder sb = new StringBuilder();

        sb.append(path);
        for(Object key : keyList){
            sb.append(seperator);
            sb.append(key);
        }

        return sb.toString();
    }
}
