package com.zxg.jwt_login.task;


public class Test extends MyTask{
    private String name;

    public Test(String name, long time) {
        super(name, time);
    }

    public Test(String name, long time, String name1) {
        super(name, time);
        this.name = name1;
    }

    @Override
    public void run() {
        System.out.println(System.currentTimeMillis()+"任务名：" + this.name);
    }
}
