package com.zxg.back.config;

import org.springframework.context.annotation.Configuration;
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
 */

@Configuration
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
