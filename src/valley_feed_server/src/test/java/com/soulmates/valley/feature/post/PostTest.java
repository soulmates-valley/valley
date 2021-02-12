package com.soulmates.valley.feature.post;

import com.soulmates.valley.config.IntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
public class PostTest extends IntegrationTest {

    @Test
    @DisplayName("포스팅 업로드 (타입 - 코드)")
    public void uploadPost_TypeCode_then_success() throws Exception {
        //given
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("content", "테스트내용테스트내용");
        parameters.add("code", "printf(helloworld)");
        parameters.add("hashTag[0]", "태그1");
        parameters.add("hashTag[1]", "태그2");

        this.mockMvc
                .perform(post("/api/post")
                .contentType("multipart/form-data")
                .params(parameters))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
