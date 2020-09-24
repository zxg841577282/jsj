package com.zxg.security.config;

import com.zxg.security.data.service.impl.SysUserServiceImpl;
import com.zxg.security.handler.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * @Author: zhou_xg
 * @Date: 2020/4/15
 * @Purpose: Security核心配置
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginFailHandler loginFailHandler;
    private final LoginSuccessHandler loginSuccessHandler;
    private final MyLogoutSuccessHandler myLogoutSuccessHandler;
    private final SysUserServiceImpl sysUserService;

    @Value("${spring.security.state}")
    private Boolean state;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * 核心功能
     * 控制请求的拦截与开放
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (state){
            http
                    .cors() // 允许跨域资源共享
                    .and()
                    .csrf().disable() //.disable()//跨站请求伪造
                    .authorizeRequests() // 授权配置
                    .anyRequest()
                    .authenticated()// 所有请求都需要认证
                    .and()
                    .formLogin() // 登陆接口
                    .loginProcessingUrl("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(loginSuccessHandler)  //登陆成功处理
                    .failureHandler(loginFailHandler)     //登陆失败处理
                    .permitAll()
                    .and()
                    .logout().logoutUrl("/logout") // 登出接口
                    .logoutSuccessHandler(myLogoutSuccessHandler)
                    .permitAll()

//                    .and()
//                    .exceptionHandling() // 异常处理设置
//                    .authenticationEntryPoint(new MyAuthExceptionEntryPoint())

                    .and()
                    .sessionManagement()    //设置session管理器
                    .maximumSessions(1)     //设置session最大数为1
                    .sessionRegistry(sessionRegistry())//存储规则
                    .expiredSessionStrategy(new SessionExampleHandler()) //session异常处理
            ;
        }else {
            http
                    .cors() // 允许跨域资源共享
                    .and()
                    .csrf().disable() //.disable()//跨站请求伪造
                    .authorizeRequests() // 授权配置
                    .anyRequest()
                    .permitAll()      //全部放开
            ;
        }


    }

    /**
     * @Description: 配置放行的资源
     **/
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                //开放静态资源
                .antMatchers("/static/**", "/favicon.ico")
                // 给 swagger 放行；不需要权限能访问的资源
                .antMatchers(
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/api-docs"
                )
        ;
    }

    /**
     * 指定认证对象的来源
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(sysUserService).passwordEncoder(passwordEncoder());

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(sysUserService);

        provider.setPasswordEncoder(passwordEncoder());

        provider.afterPropertiesSet();
        provider.setHideUserNotFoundExceptions(false);

        auth.authenticationProvider(provider);
    }


}
