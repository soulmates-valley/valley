package com.soulmates.valley.common.util;

import com.soulmates.valley.dto.common.UserInfo;
import com.soulmates.valley.testconfig.MockTest;
import com.soulmates.valley.testconfig.example.TokenExample;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class JWTParserTest extends MockTest {

    @Test
    void getUidFromJWT(){
        // given
        String accessToken = new TokenExample().example();

        // when
        UserInfo userInfo = JWTParser.getUsersFromJWT(accessToken);

        // then
        assertThat(userInfo.getUserId()).isEqualTo(68);
        assertThat(userInfo.getNickname()).isEqualTo("before");
        assertThat(userInfo.getUserEmail()).isEqualTo("profile3@test.com");
    }
}
