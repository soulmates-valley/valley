package com.soulmates.valley.common.util;

import com.soulmates.valley.common.dto.Users;
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
        Users users = JWTParser.getUsersFromJWT(accessToken);

        // then
        assertThat(users.getUserId()).isEqualTo(68);
        assertThat(users.getNickname()).isEqualTo("before");
        assertThat(users.getUserEmail()).isEqualTo("profile3@test.com");
    }
}
