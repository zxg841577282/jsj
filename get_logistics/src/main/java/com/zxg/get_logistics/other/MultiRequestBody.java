package com.zxg.get_logistics.other;

import java.lang.annotation.*;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/21 0021
 * @Purpose:
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiRequestBody {
    /**
     * 是否必须出现的参数
     */
    boolean required() default true;

    /**
     * 当value的值或者参数名不匹配时，是否允许解析最外层属性到该对象
     */
    boolean parseAllFields() default true;

    /**
     * 解析时用到的JSON的key
     */
    String value() default "";
}
