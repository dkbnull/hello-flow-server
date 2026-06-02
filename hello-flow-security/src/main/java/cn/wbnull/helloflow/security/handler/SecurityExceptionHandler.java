/*
 * Copyright (c) 2017-2026 null. All rights reserved.
 */

package cn.wbnull.helloflow.security.handler;

import cn.wbnull.helloflow.common.model.Result;
import cn.wbnull.helloflow.common.model.ResultCode;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 安全异常处理器
 *
 * @author null
 * @date 2026-05-26
 */
@RestControllerAdvice
@Order(1)
public class SecurityExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBadCredentialsException(BadCredentialsException e) {
        return Result.fail(ResultCode.LOGIN_FAILED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        return Result.fail(ResultCode.FORBIDDEN);
    }
}
