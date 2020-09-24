package com.zxg.back.handle;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/16
 * @Purpose: 退出登陆
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    protected Logger logger = LoggerFactory.getLogger(MyLogoutSuccessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("LoginSuccessHandler：{}", "用户登陆成功");

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("code", HttpStatus.OK.value());
        resMap.put("msg", "退出登陆成功");

        response.getWriter().write(JSONObject.toJSONString(resMap));
        response.flushBuffer();
    }
}
