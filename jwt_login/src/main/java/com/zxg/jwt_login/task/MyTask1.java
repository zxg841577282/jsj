package com.zxg.jwt_login.task;


public class MyTask1 extends Task {
    private final String name;

    /**
     * 新增构造方法
     * @param name 任务名
     * @param time 延迟时间 单位毫秒
     */
    public MyTask1(String name, long time) {
        super(name, time);
        this.name = name;
    }

    /**
     * 删除构造方法 (因修改了equals和hashcode方法,仅比较任务名)
     * @param name 任务名
     */
    public MyTask1(String name){
        super("MyTask1-" + name);
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("MyTask1任务名：" + this.name + "启动");
    }
}
