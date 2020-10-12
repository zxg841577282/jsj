package com.zxg.jwt_login.common.handler;

import com.zxg.jwt_login.common.jwt.JwtUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserHandler implements HandlerMethodArgumentResolver {
    private static String LOGIN_TOKEN_KEY = "X-Litemall-Token";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(Integer.class) && methodParameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Integer resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest request, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        if (token == null || token.isEmpty()) {
            return null;
        }

        return new JwtUtil().getUserId(token);
    }
}
