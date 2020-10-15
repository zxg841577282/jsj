package com.zxg.jwt_login.common.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 * 通过自定义参数解析器(LoginUserHandler) 设置赋值规则
 * 用于控制器上参数Integer userId的赋值
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {

}
