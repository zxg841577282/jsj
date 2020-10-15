package com.zxg.jwt_login.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class aaaa {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 6; i++) {
            final int j=i;
            executorService.execute(()->{
                System.out.println(Thread.currentThread().getName()+" "+j);
                if (j == 3){
                    throw new RuntimeException("手动异常");
                }
            });
        }
    }

}
