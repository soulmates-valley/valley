package com.soulmates.valley.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.common.dto.UserInfo;
import com.soulmates.valley.common.util.JWTParser;
import com.soulmates.valley.dto.feed.FeedLimitRequest;
import com.soulmates.valley.service.HomeFeedService;
import com.soulmates.valley.dto.posting.PostDetail;
import com.soulmates.valley.testconfig.example.PostEaxample;
import com.soulmates.valley.testconfig.example.TokenExample;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeFeedController.class)
public class HomeFeedControllerTest {

    private static final String BASE_URL = "/feed";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private HomeFeedService homeFeedService;

    private String token;

    private List<PostDetail> postDetailList;

    @BeforeEach
    void setUp(){
        token = new TokenExample().example();
        UserInfo currentUser = JWTParser.getUsersFromJWT(token);
        postDetailList = new ArrayList<>();
        postDetailList.add(new PostEaxample().example(currentUser));
        postDetailList.add(new PostEaxample().example2(currentUser));
    }

    @DisplayName("홈피드 조회")
    @Nested
    class GetFeedPostAll{
        @DisplayName("[정상] 조회성공")
        @Test
        void getFeedAll() throws Exception {
            // given
            Long postId = postDetailList.get(0).getPostId();
            FeedLimitRequest feedLimitRequest = FeedLimitRequest.builder()
                    .page(1L)
                    .size(3L).build();

            // when
            ResultActions resultActions = requestGetFeedPost(token, postId, feedLimitRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()));
        }

        private ResultActions requestGetFeedPost(String token, Long postId, FeedLimitRequest feedLimitRequest) throws Exception {
            return mockMvc.perform(get(BASE_URL + "/all")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("postId", postId + "")
                    .param("page", feedLimitRequest.getPage() + "")
                    .param("size", feedLimitRequest.getSize() + "")
                    .header(HttpHeaders.AUTHORIZATION, token))
                    .andDo(print());
        }
    }
}
