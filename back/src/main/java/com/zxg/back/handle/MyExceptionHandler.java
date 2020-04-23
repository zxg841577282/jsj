package com.zxg.back.handle;

import com.zxg.back.web.resp.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        R r = new R();
        r.put("code", 403);
        r.put("msg", "暂无权限");

        return r;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        return R.error(404, e.getBindingResult().getFieldError().getDefaultMessage());
    }


    @ExceptionHandler( NullPointerException.class)
    public R MethodNullPointerException( NullPointerException e) {
        logger.error(e.getMessage(), e);
        return R.error(404, "数据为空异常");
    }


    @ExceptionHandler(Exception.class)
    public R MethodException(Exception e) {
        logger.error(e.getMessage(), e);
        return R.error(404, "未知异常");
    }


}
