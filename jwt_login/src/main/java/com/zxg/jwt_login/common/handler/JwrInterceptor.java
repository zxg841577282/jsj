package com.zxg.jwt_login.common.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.zxg.jwt_login.common.jwt.JwtUtil;
import com.zxg.jwt_login.common.other.CommonRedisKey;
import com.zxg.jwt_login.common.other.ResultCode;
import com.zxg.jwt_login.common.other.ThreadMap;
import com.zxg.jwt_login.data.domain.AdmUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Log4j2
@Service
public class JwrInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,  Object handler) throws Exception {
        String token = request.getHeader(LoginUserHandler.LOGIN_TOKEN_KEY);
        if (ObjectUtil.isEmpty(token)) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSONUtil.toJsonStr(ResultCode.CLIENT_UN_CARRIED_VOUCHER));
            return false;
        }
        Integer userId = new JwtUtil().getUserId(token);
        AdmUser user = new AdmUser().selectById(userId);
        if (ObjectUtil.isEmpty(user)) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSONUtil.toJsonStr(ResultCode.INVALID_CREDENTIAL));
            return false;
        }
        String redisToken = stringRedisTemplate.opsForValue().get(String.format(CommonRedisKey.LOGIN_TOKEN, userId));
        if (ObjectUtil.isEmpty(redisToken)) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSONUtil.toJsonStr(ResultCode.LOGIN_OUT));
            return false;
        }
        ThreadMap.threadLocal.set(user);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ThreadMap.remove();
    }
}
