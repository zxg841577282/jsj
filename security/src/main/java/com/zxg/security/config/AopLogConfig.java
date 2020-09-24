package com.zxg.security.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import other.Group;
import other.R;
import other.ResultException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/17
 * @Purpose: AOP记录请求
 */
@Aspect
@Component
@Slf4j
public class AopLogConfig {
    private static final String START_TIME = "request-start";
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final ExecutableValidator methodValidator = factory.getValidator().forExecutables();
    private static final Validator beanValidator = factory.getValidator();


    /**
     * 切入点
     */
    @Pointcut("execution(public * com.zxg.security.*.*Controller.*(..))")
    public void log() {

    }

    /**
     * 前置操作
     *
     * @param point 切入点
     */
    @Before("log()")
    public void beforeLog(JoinPoint point) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        log.info("【请求 URL】：{}", request.getRequestURL());
        log.info("【请求 IP】：{}", request.getRemoteAddr());
        log.info("【请求类名】：{}，【请求方法名】：{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName());

        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("【请求参数】：{}，", JSON.toJSONString(parameterMap));
        Long start = System.currentTimeMillis();
        request.setAttribute(START_TIME, start);


        Object[] args = point.getArgs();
        Object target = point.getThis();

        // 此处可以抛个异常提示用户参数输入格式不正确
        MethodSignature joinPointObject = (MethodSignature) point.getSignature();
        Method method = joinPointObject.getMethod();
        validMethodParams(target, method, args).forEach(aValidResult -> {
            throw new ResultException(aValidResult.getMessage());
        });

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations != null && parameterAnnotations.length != 0) {
            for (int i = 0; i < parameterAnnotations.length; i++) {
                // 获取参数bean
                Object arg = args[i];
                Annotation[] parameterAnnotation = parameterAnnotations[i];
                // 交给group校验
                boolean hasGroup = isHasGroup(arg, parameterAnnotation);
                // 交给bean校验
                noGroupValid(arg, hasGroup);
            }
        }


    }

    /**
     * 环绕操作
     *
     * @param point 切入点
     * @return 原方法返回值
     * @throws Throwable 异常信息
     */
    @Around("log()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {
        try {
            Object result = point.proceed();
            log.info("【返回值】：{}", JSON.toJSONString(result));
            return result;
        }catch (ResultException e){
            log.error(e.getMessage());
            return R.error(e.getMessage());
        }
    }

    /**
     * 后置操作
     */
    @AfterReturning("log()")
    public void afterReturning() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        Long start = (Long) request.getAttribute(START_TIME);
        Long end = System.currentTimeMillis();
        log.info("【请求耗时】：{}毫秒", end - start);

//        String header = request.getHeader("User-Agent");
//        UserAgent userAgent = UserAgent.parseUserAgentString(header);
//        log.info("【浏览器类型】：{}，【操作系统】：{}，【原始User-Agent】：{}", userAgent.getBrowser().toString(), userAgent.getOperatingSystem().toString(), header);
    }


    private void noGroupValid(Object arg, boolean hasGroup) {
        if (!hasGroup && arg != null){
            validBeanParams(arg).forEach(aValidResult -> {
                aValidResult.getMessage();
                throw new ResultException(aValidResult.getMessage());
            });
        }
    }

    private boolean isHasGroup(Object arg, Annotation[] parameterAnnotation) {
        for (Annotation annotation : parameterAnnotation) {
            if (annotation instanceof Group) {
                Group group = (Group) annotation;
                Class value = group.value();
                validBeanParamsGroup(arg,value).forEach(aValidResult -> {
                    throw new ResultException(aValidResult.getMessage());
                });
                return true;
            }
        }
        return false;
    }

    private <T> Set<ConstraintViolation<T>> validBeanParams(T bean) {
        return beanValidator.validate(bean);
    }

    private <T> Set<ConstraintViolation<T>> validBeanParamsGroup(T bean, Class... group) {
        return beanValidator.validate(bean,group);
    }

    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object[] params) {
        return methodValidator.validateParameters(obj, method, params);
    }
}
