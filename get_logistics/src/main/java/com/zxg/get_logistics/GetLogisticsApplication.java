package com.zxg.get_logistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class},
        scanBasePackages={
        "com.zxg.get_logistics","util","config"
})
public class GetLogisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GetLogisticsApplication.class, args);
    }

}
