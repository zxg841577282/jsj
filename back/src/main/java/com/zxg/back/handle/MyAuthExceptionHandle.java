package com.zxg.back.handle;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class MyAuthExceptionHandle implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(MyAuthExceptionHandle.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        logger.warn("MyAuthExceptionHandle:{}",authException.getMessage());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("code",HttpStatus.UNAUTHORIZED.value());
        resMap.put("msg","未登录或token已失效");

        response.getWriter().write(JSONObject.toJSONString(resMap));
        response.flushBuffer();
    }
}
