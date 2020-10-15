package com.zxg.jwt_login.task;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;

@Component
public class TaskService {
    private TaskService taskService;
    private DelayQueue<MyTask> delayQueue =  new DelayQueue<MyTask>();

    @PostConstruct
    private void init() {
        taskService = this;

        //单线程线程池
        //这个线程池可以在线程死后（或发生异常时）重新启动一个线程来替代原来的线程继续执行下去
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        MyTask task = delayQueue.take();
                        task.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void addTask(MyTask task){
        if(delayQueue.contains(task)){
            return;
        }
        delayQueue.add(task);
    }

    public void removeTask(MyTask task){
        delayQueue.remove(task);
    }

}
