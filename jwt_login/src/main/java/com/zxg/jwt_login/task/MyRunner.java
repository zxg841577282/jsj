package com.zxg.jwt_login.task;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 实现ApplicationRunner 在项目启动完成后执行run()方法
 */

@Component
public class MyRunner implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("系统启动自动加载");
    }
}
