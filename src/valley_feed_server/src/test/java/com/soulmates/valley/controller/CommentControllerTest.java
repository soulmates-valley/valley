package com.soulmates.valley.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.dto.common.UserInfo;
import com.soulmates.valley.common.util.JWTParser;
import com.soulmates.valley.dto.comment.CommentAddRequest;
import com.soulmates.valley.dto.comment.CommentInfo;
import com.soulmates.valley.dto.comment.CommentPageLimitReuqest;
import com.soulmates.valley.service.CommentService;
import com.soulmates.valley.testconfig.example.CommentExample;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    private static final String BASE_URL = "/comment";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    private String token;
    private CommentInfo comment1;
    private CommentInfo comment2;

    @BeforeEach
    void setUp(){
        token = new TokenExample().example();
        UserInfo currentUser = JWTParser.getUsersFromJWT(token);
        comment1 = new CommentExample().example(currentUser);
        comment2 = new CommentExample().example2(currentUser);
    }

    @DisplayName("포스트 댓글 등록")
    @Nested
    class AddCommentToPost{
        @DisplayName("[정상] 등록성공")
        @Test
        void create() throws Exception {
            // given
            CommentAddRequest commentAddRequest = CommentAddRequest.builder()
                    .content("hi")
                    .postId(2L).build();

            // when
            ResultActions resultActions = requestAddComment(token, commentAddRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()));
        }

        @DisplayName("[오류] content 빈 값으로 요청한 경우")
        @Test
        void contentIsNull() throws Exception {
            // given
            CommentAddRequest commentAddRequest = CommentAddRequest.builder()
                    .postId(2L).build();

            // when
            ResultActions resultActions = requestAddComment(token, commentAddRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(ResponseCode.PARAM_INVALID.getCode()));
        }

        private ResultActions requestAddComment(String token, CommentAddRequest commentAddRequest) throws Exception {
            return mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .content(objectMapper.writeValueAsString(commentAddRequest)))
                    .andDo(print());
        }
    }

    @DisplayName("특정 post의 댓글 조회")
    @Nested
    class GetCommentFromPost{
        @DisplayName("[정상] 조회성공")
        @Test
        void getComment() throws Exception {
            // given
            Long postId = 11L;
            CommentPageLimitReuqest commentPageLimitReuqest = CommentPageLimitReuqest.builder()
                    .page(1)
                    .size(10).build();
            List<CommentInfo> response = new ArrayList<>();
            response.add(comment1);
            response.add(comment2);
            given(commentService.getCommentFromPost(anyLong(), anyInt(), anyInt())).willReturn(response);

            // when
            ResultActions resultActions = requestGetComment(token, postId, commentPageLimitReuqest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()));
        }

        private ResultActions requestGetComment(String token, Long postId, CommentPageLimitReuqest commentPageLimitReuqest) throws Exception {
            return mockMvc.perform(get(BASE_URL + "/all")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("postId", postId + "")
                    .param("page", commentPageLimitReuqest.getPage() + "")
                    .param("size", commentPageLimitReuqest.getSize() + "")
                    .header(HttpHeaders.AUTHORIZATION, token))
                    .andDo(print());
        }
    }
}

