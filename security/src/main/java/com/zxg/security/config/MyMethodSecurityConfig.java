package com.zxg.security.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义决策器
 * 加载项目中带有注解PreAuthorize的value
 * 使用自定义投票器MyAccessVoter进行判断当前登陆账户是否有相应等权限
 */


@EnableGlobalMethodSecurity(prePostEnabled = true)      //开启PreAuthorize注解权限判断
public class MyMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected AccessDecisionManager accessDecisionManager() {

        //直接按照prePostEnabled的方式
        ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
        expressionAdvice.setExpressionHandler(getExpressionHandler());
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();

        //使用自定义投票器
        decisionVoters.add(new MyAccessVoter(expressionAdvice));

        return new AffirmativeBased(decisionVoters);
    }
}
