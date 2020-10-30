package com.zxg.job.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxg.job.domain.SysJob;
import com.zxg.job.utils.ScheduleUtils;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("InitService")
public class InitService implements ApplicationRunner {
    @Autowired
    private Scheduler scheduler;

    /**
     * 项目启动时，初始化定时器
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //1.查询所有已运行的任务
        List<SysJob> lqlRobotRules = new SysJob().selectList(new LambdaQueryWrapper<SysJob>()
                .eq(SysJob::getState, SysJob.RobotRuleState.RUNNABLE.getCode())
        );

        for (SysJob sysJob : lqlRobotRules) {
            Trigger trigger = ScheduleUtils.getTrigger(scheduler, sysJob.getId());
            //如果不存在，则创建
            if (ObjectUtil.isEmpty(trigger)) {
                ScheduleUtils.createScheduleJob(scheduler, sysJob);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, sysJob);
            }
        }
    }
}
