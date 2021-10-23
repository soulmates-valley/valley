package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.ResponseCode;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<CommonResponse<Object>> handleCustomException(CustomException ex) {
        ResponseCode responseCode = ex.getResponseCode();
        log.info("--- [{}] 시스템 오류 감지 : {}", responseCode.getCode(), responseCode.getMessage(), ex);
        return ResponseEntity.ok(new CommonResponse<>(responseCode));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<CommonResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> fieldErrorMessage = getFieldErrorMessage(e.getFieldErrors());
        CommonResponse<Object> response = new CommonResponse<>(ResponseCode.PARAM_INVALID, fieldErrorMessage);
        log.error("Argument is wrong: {}", response.getData());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("--- 알 수 없는 오류 감지.  ", ex);
        return ResponseEntity.ok(new CommonResponse<>(ResponseCode.ETC));
    }

    private List<String> getFieldErrorMessage(List<FieldError> fieldErrors) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : fieldErrors) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        return errors;
    }
}
