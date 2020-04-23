package com.zxg.back;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(
        scanBasePackages={
                "mapper","com.zxg.back","util","config"
        }
)
@MapperScan(basePackages = "mapper")
@EnableSwagger2
public class BackApplication  {

    public static void main(String[] args) {
        SpringApplication.run(BackApplication.class, args);
    }
}
