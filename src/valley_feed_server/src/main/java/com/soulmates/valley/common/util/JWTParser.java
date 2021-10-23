package com.soulmates.valley.common.util;

import com.soulmates.valley.common.dto.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class JWTParser {

    private static final String ENCODED_KEY = "a@u#t%hse#rver0102test!@#%";

    private JWTParser(){}

    public static Long getUidFromJWT(String token) {
        return Long.parseLong(String.valueOf(parseJWT(token).get("userId")));
    }

    public static Users getUsersFromJWT(String token){
        Claims claims = parseJWT(token);
        return Users.builder()
                .nickname(String.valueOf(claims.get("nickname")))
                .userEmail(String.valueOf(claims.get("userEmail")))
                .userId(Long.valueOf(String.valueOf(claims.get("userId")))).build();
    }

    private static Claims parseJWT(String jwt) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    private static SecretKey generalKey() {
        byte[] encodeKeyChar = ENCODED_KEY.getBytes();
        return new SecretKeySpec(encodeKeyChar, 0, encodeKeyChar.length, "HS256");
    }
}
