package com.soulmates.valley.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulmates.valley.common.constants.ErrorEnum;
import com.soulmates.valley.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler({CustomException.class})
    public void handleCustomException(HttpServletRequest request, HttpServletResponse response, CustomException ex) throws IOException {
        ErrorEnum errorEnum = ex.getErrorEnum();

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(new CommonResponse(errorEnum)));

        log.info("[{}] 시스템 오류 감지 : {}", errorEnum.getErrCode(), errorEnum.getMessage(), ex);
    }

    @ExceptionHandler({Exception.class})
    public void handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        ex.printStackTrace();

        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");
        logger.error("알 수 없는 오류 감지.", ex);
    }
}
