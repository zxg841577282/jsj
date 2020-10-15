package com.zxg.jwt_login.common.handler;

import com.zxg.jwt_login.common.jwt.JwtUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 自定义参数解析器
 */
public class LoginUserHandler implements HandlerMethodArgumentResolver {
    public static String LOGIN_TOKEN_KEY = "token";

    /**
     * 支持的参数 可以设置一些标志，表示你这个分解器可以处理这些参数，返回ture才执行resolveArgument()函数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(Integer.class) && methodParameter.hasParameterAnnotation(LoginUser.class);
    }

    /**
     * 分解实参 处理实参的具体方法
     */
    @Override
    public Integer resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest request, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        if (token == null || token.isEmpty()) {
            return null;
        }

        return new JwtUtil().getUserId(token);
    }
}
