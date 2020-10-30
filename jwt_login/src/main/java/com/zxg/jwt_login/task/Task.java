package com.zxg.jwt_login.task;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


/**
 * 自定义延迟任务
 * 需重写equals 和hashcode方法
 */

public abstract class Task implements Delayed ,Runnable{
    //名称
    private final String name ;
    //开始时间
    private final long start = System.currentTimeMillis();
    //过期时间 毫秒
    private long time ;

    public Task(String name) {
        this.name = name;
    }

    public Task(String name, long time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    /**
     * 获取延迟时间 过期时间-当前时间
     */
    @Override
    public long getDelay(@NotNull TimeUnit unit) {
        return unit.convert((start+time) - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    /**
     * 用于延迟队列内部比较排序   当前对象的延迟时间 - 比较对象的延迟时间
     */
    @Override
    public int compareTo(@NotNull Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task myTask = (Task) o;
        //只比较任务名称
        return Objects.equals(name, myTask.name);
    }

    @Override
    public int hashCode() {
        //只比较任务名称的哈希值
        return Objects.hash(name);
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
