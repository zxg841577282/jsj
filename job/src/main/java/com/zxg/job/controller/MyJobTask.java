package com.zxg.job.controller;

import com.alibaba.fastjson.JSONObject;
import com.zxg.job.domain.SysJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;



@Slf4j
@Component("MyJobTask")
public class MyJobTask {

    /**
     * 任务一
     */
    public void JobOne(String params) {
        SysJob sysJob = JSONObject.parseObject(params, SysJob.class);
        System.out.println(sysJob.toString());
    }

}
