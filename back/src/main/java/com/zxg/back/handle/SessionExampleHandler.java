package com.zxg.back.handle;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/16
 * @Purpose: session登陆
 */

@Component
public class SessionExampleHandler implements SessionInformationExpiredStrategy {

    private static final Logger logger = LoggerFactory.getLogger(SessionExampleHandler.class);

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        logger.warn("SessionExampleHandle:{}", "你的账号在另一地点被登录");

        HttpServletResponse response = event.getResponse();

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("code", HttpStatus.UNAUTHORIZED.value());
        resMap.put("msg", "你的账号在另一地点被登录");

        response.getWriter().write(JSONObject.toJSONString(resMap));
        response.flushBuffer();
    }
}
