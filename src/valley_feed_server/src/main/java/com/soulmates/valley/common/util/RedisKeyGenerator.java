package com.soulmates.valley.common.util;

public class RedisKeyGenerator {

    private RedisKeyGenerator(){}

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
