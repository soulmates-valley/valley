package com.soulmates.valley.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulmates.valley.common.constants.ErrorEnum;
import com.soulmates.valley.common.exception.CustomException;
import com.soulmates.valley.feature.comment.dto.CommentAddRequest;
import com.soulmates.valley.feature.comment.service.CommentService;
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

    @DisplayName("포스트 댓글 등록")
    @Nested
    class AddCommentToPost{
        @DisplayName("[오류] content 빈 값으로 요청한 경우")
        @Test
        void contentIsNull() throws Exception {
            // given
            String token = "1111";
            CommentAddRequest commentAddRequest = CommentAddRequest.builder()
                    .postId(2L).build();

            // when
            ResultActions resultActions = requestAddComment(token, commentAddRequest);

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(ErrorEnum.PARAM_INVALID.getErrCode()));
        }

        private ResultActions requestAddComment(String token, CommentAddRequest commentAddRequest) throws Exception {
            return mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION,token)
                    .content(objectMapper.writeValueAsString(commentAddRequest)))
                    .andDo(print());
        }
    }
}
