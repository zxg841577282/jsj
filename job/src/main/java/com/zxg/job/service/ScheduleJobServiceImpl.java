package com.zxg.job.service;

import com.zxg.job.domain.SysJob;
import com.zxg.job.dto.AddModelDto;
import com.zxg.job.utils.ScheduleUtils;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ScheduleJobServiceImpl implements ScheduleJobService{
    @Autowired
    private Scheduler scheduler;

    @Override
    public boolean addModel(AddModelDto dto) {

        if (dto.getRunType().equals(SysJob.RobotRuleRunType.CORN.getCode())){

            SysJob cornSysJob = new SysJob(dto.getCornJob());

            ScheduleUtils.createScheduleJob(scheduler, cornSysJob);
        }else {
            SysJob cornSysJob = new SysJob(dto.getBaseJob());

            ScheduleUtils.createScheduleJob(scheduler, cornSysJob);
        }


        return false;
    }

    @Override
    public boolean updateModel() {
        return false;
    }
}
