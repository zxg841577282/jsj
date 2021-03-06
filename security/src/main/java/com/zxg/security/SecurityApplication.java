package com.zxg.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(
        scanBasePackages = {
                "com.zxg.security.data.mapper","com.zxg.security", "util", "config"
        }
)
@EnableSwagger2
@MapperScan(basePackages = "com.zxg.security.data.mapper")
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

}
