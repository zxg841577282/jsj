package com.zxg.security.handler;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/16
 * @Purpose: 登陆失败
 */
@Component
public class LoginFailHandler implements AuthenticationFailureHandler {

    protected Logger logger = LoggerFactory.getLogger(LoginFailHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        logger.info("LoginFailHandler：{}", e.getMessage());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("code", HttpStatus.UNAUTHORIZED.value());

        String msg;

        if (e instanceof BadCredentialsException) {
            msg = "密码输入错误!";
        } else if (e instanceof UsernameNotFoundException) {
            msg = "账户名不存在";
        } else if (e instanceof LockedException) {
            msg = "账户被锁定，请联系管理员!";
        } else if (e instanceof CredentialsExpiredException) {
            msg = "密码过期，请联系管理员!";
        } else if (e instanceof AccountExpiredException) {
            msg = "账户过期，请联系管理员!";
        } else if (e instanceof DisabledException) {
            msg = "账户被禁用，请联系管理员!";
        } else {
            msg = "登录失败!";
        }
        resMap.put("msg", msg);

        response.getWriter().write(JSONObject.toJSONString(resMap));
        response.flushBuffer();
    }
}
