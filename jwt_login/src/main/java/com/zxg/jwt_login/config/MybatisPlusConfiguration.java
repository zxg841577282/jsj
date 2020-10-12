package com.zxg.jwt_login.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangjian
 * @date 2019/10/9
 */
@Configuration
@MapperScan({"org.linlinjava.litemall.db.dao.**"})
public class MybatisPlusConfiguration {
    public MybatisPlusConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean({PaginationInterceptor.class})
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置方言
        paginationInterceptor.setDialectType("mysql");
        return paginationInterceptor;
    }
}
