package com.zxg.jwt_login.task;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


/**
 * 自定义延迟任务
 */

public abstract class MyTask implements Delayed ,Runnable{
    //名称
    private String name ;
    //开始时间
    private long start = System.currentTimeMillis();
    //过期时间 毫秒
    private long time ;

    public MyTask(String name, long time) {
        this.name = name;
        this.time = time;
    }


    /**
     * 获取延迟时间 过期时间-当前时间
     * @param unit
     * @return
     */
    @Override
    public long getDelay(@NotNull TimeUnit unit) {
        return unit.convert((start+time) - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    /**
     * 用于延迟队列内部比较排序   当前对象的延迟时间 - 比较对象的延迟时间
     * @param o 目标对象
     * @return
     */
    @Override
    public int compareTo(@NotNull Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return "MyTask{" +
                "name='" + name + '\'' +
                ", start=" + start +
                ", time=" + time +
                '}';
    }
}
