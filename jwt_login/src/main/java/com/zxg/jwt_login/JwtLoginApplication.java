package com.zxg.jwt_login;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.zxg.jwt_login.dao")
@SpringBootApplication
public class JwtLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtLoginApplication.class, args);
    }

}
