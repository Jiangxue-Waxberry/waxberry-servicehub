package com.jiangxue.framework.common.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResult<String> handleException(Exception e) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);
        return ApiResult.error(e.getMessage());
    }
}
