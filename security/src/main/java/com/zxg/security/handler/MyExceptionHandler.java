package com.zxg.security.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import other.R;

/**
 * @Author: zhou_xg
 * @Date: 2019/12/19 16:40
 * @Purpose: 通用异常拦截
 */
@RestControllerAdvice
public class MyExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);


    @ExceptionHandler(AccessDeniedException.class)
    public R handleAccessDeniedException(AccessDeniedException e){
        return R.error(401, "暂无访问权限");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        return R.error(403, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler( NullPointerException.class)
    public R MethodNullPointerException( NullPointerException e) {
        logger.error(e.getMessage(), e);
        return R.error(404, "数据为空异常");
    }

    @ExceptionHandler(Exception.class)
    public R MethodException(Exception e) {
        logger.error(e.getMessage(), e);
        return R.error(404, e.getMessage());
    }


}
