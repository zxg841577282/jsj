package com.zxg.jwt_login.task;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;

/**
 * 延迟任务service
 */

@Component
public class TaskService {
    private TaskService taskService;

    private final DelayQueue<Task> delayQueue =  new DelayQueue<>();

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
                        delayQueue.take().run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 新增延迟任务
     *
     * add 与 offer 比较
     * 前者插入队列尾部,若队列已满,则抛出IllegalStateException 异常
     * 后者插入队列尾部,若队列已满,则返回false
     * put 方法 等待队列可用后插入尾部
     */
    public boolean addTask(Task task){
        if(delayQueue.contains(task)){
            System.out.println("延迟队列中已存在相同任务");
            return false;
        }
        System.out.println("新增前延迟队列数量："+ delayQueue.size());

        System.out.println("创建新队列："+ task.getName());

        boolean offer = delayQueue.offer(task);

        System.out.println("新增后延迟队列数量："+ delayQueue.size());

        return offer;

    }

    /**
     * 删除延迟任务
     */
    public boolean removeTask(Task task){
        if(delayQueue.contains(task)){
            System.out.println("延迟队列中不存在对应任务");
            return false;
        }

        System.out.println("删除前延迟队列数量："+delayQueue.size());

        System.out.println("删除队列："+ task.getName());

        boolean remove = delayQueue.remove(task);

        System.out.println("删除后延迟队列数量："+delayQueue.size());

        return remove;
    }

    /**
     * 清空延迟任务
     */
    public void clear(){
        System.out.println("清空前延迟队列数量："+delayQueue.size());

        delayQueue.clear();

        System.out.println("清空后延迟队列数量："+delayQueue.size());
    }

    /**
     * 返回队列中所有元素的数组
     */
    public Object[] toArray(){
        return delayQueue.toArray();
    }

}
