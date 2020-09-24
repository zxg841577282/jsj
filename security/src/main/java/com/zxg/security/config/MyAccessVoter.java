package com.zxg.security.config;

import com.alibaba.fastjson.JSONObject;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.prepost.PreInvocationAttribute;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdvice;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.Iterator;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/17
 * @Purpose: 自定义投票器
 */

public class MyAccessVoter implements AccessDecisionVoter<MethodInvocation> {

    private final PreInvocationAuthorizationAdvice preAdvice;

    public MyAccessVoter(PreInvocationAuthorizationAdvice pre) {
        this.preAdvice = pre;
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    /**
     * 本投票操作仅支持 PrePostEnabled
     * @param authentication
     * @param method
     * @param collection
     * @return
     */
    @Value(value = "${spring.security.state}")
    private boolean securityState;

    @Override
    public int vote(Authentication authentication, MethodInvocation method, Collection<ConfigAttribute> collection) {
        if (!securityState){
            return 1;
        }

        // 没有权限默认拒绝
        if (authentication == null){
            return ACCESS_DENIED;
        }

        // 匿名用户 不允许
        if (authentication instanceof AnonymousAuthenticationToken) {
            return ACCESS_DENIED;
        }

        //获取数据库配置权限
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(authentication.getPrincipal()));
        if ("zxg".equals(jsonObject.getString("account"))) {
            return ACCESS_GRANTED;
        }

        PreInvocationAttribute preAttr = findPreInvocationAttribute(collection);
        if (preAttr == null) {
            return 0;
        } else {
            boolean allowed = this.preAdvice.before(authentication, method, preAttr);
            return allowed ? 1 : -1;
        }
    }

    private PreInvocationAttribute findPreInvocationAttribute(Collection<ConfigAttribute> config) {
        Iterator var2 = config.iterator();

        ConfigAttribute attribute;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            attribute = (ConfigAttribute)var2.next();
        } while(!(attribute instanceof PreInvocationAttribute));

        return (PreInvocationAttribute)attribute;
    }

}
