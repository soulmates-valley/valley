package com.soulmates.valley.controller;

import com.soulmates.valley.common.constants.ErrorEnum;
import com.soulmates.valley.common.dto.CommonResponse;
import com.soulmates.valley.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<CommonResponse> handleCustomException(CustomException ex) {
        ErrorEnum errorEnum = ex.getErrorEnum();
        log.info("--- [{}] 시스템 오류 감지 : {}", errorEnum.getErrCode(), errorEnum.getMessage(), ex);
        return ResponseEntity.ok(new CommonResponse(errorEnum));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<CommonResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> fieldErrorMessage = getFieldErrorMessage(e.getFieldErrors());

        CommonResponse response = CommonResponse.builder()
                .code(ErrorEnum.PARAM_INVALID.getErrCode())
                .data(fieldErrorMessage).build();

        log.error("Argument is wrong: {}", response.getData());

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<CommonResponse> handleException(Exception ex) {
        log.error("--- 알 수 없는 오류 감지.  ", ex);
        return ResponseEntity.ok(new CommonResponse(ErrorEnum.ETC));
    }

    private List<String> getFieldErrorMessage(List<FieldError> fieldErrors) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : fieldErrors) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        return errors;
    }
}
