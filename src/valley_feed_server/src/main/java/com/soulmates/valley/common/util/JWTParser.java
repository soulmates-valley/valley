package com.soulmates.valley.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JWTParser {
    public static Long getUidFromJWT(String token) {
        return Long.parseLong(String.valueOf(parseJWT(token).get("userId")));
    }

    public static Claims parseJWT(String jwt) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    public static SecretKey generalKey() {
        final String encodedKey = "a@u#t%hse#rver0102test!@#%";
        byte[] encodeKeyChar = encodedKey.getBytes();
        SecretKey key = new SecretKeySpec(encodeKeyChar, 0, encodeKeyChar.length, "HS256");
        return key;
    }
}
