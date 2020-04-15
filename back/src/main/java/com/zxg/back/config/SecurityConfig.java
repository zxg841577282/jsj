package com.zxg.back.config;

import com.zxg.back.handle.MyAuthExceptionHandle;
import com.zxg.back.handle.SessionExampleHandle;
import com.zxg.back.service.adm.AdmUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/15
 * @Purpose:
 */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AdmUserService admUserService;


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * @Description: 配置放行的资源
     **/
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/index.html", "/static/**", "/favicon.ico")
                // 给 swagger 放行；不需要权限能访问的资源
                .antMatchers(
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/api-docs"
                        );
    }

    /**
     * 核心功能
     * 控制请求的拦截与开放
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors() // 允许跨域资源共享
                .and()
                .csrf().disable() //.disable()//跨站请求伪造
                .exceptionHandling() // 异常处理设置
                .authenticationEntryPoint(new MyAuthExceptionHandle())
                .and()
                .authorizeRequests() // 授权配置
                .antMatchers("/back/test/login").permitAll()//开放登陆
                .anyRequest()  // 所有请求
                .authenticated()// 都需要认证
                .and()
                .logout().permitAll()//注销允许访问
                .and()
                .sessionManagement()    //设置session管理器
                .maximumSessions(1)     //设置session最大数为1
                .expiredSessionStrategy(new SessionExampleHandle()) //session异常处理
        ;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(admUserService)
                .passwordEncoder(passwordEncoder());
    }




}
