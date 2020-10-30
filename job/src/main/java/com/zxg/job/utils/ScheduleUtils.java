package com.zxg.job.utils;

import cn.hutool.core.util.ObjectUtil;
import com.zxg.job.domain.SysJob;
import com.zxg.job.job.ScheduleJob;
import org.apache.commons.beanutils.ConvertUtils;
import org.quartz.*;

import java.time.LocalTime;

/**
 * 定时任务工具类
 */
public class ScheduleUtils {
    private final static String JOB_NAME = "TASK_";
    
    /**
     * 获取触发器key
     */
    public static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey(JOB_NAME + jobId);
    }
    
    /**
     * 获取jobKey
     */
    public static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey(JOB_NAME + jobId);
    }

    /**
     * 获取简单触发器
     */
    public static Trigger getTrigger(Scheduler scheduler, Long jobId) {
        try {
            return scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            throw new RuntimeException("获取定时任务SimpleTrigger出现异常", e);
        }
    }


    public static Trigger createCronScheduleJob(SysJob sysJob) {
        CronScheduleBuilder builder = CronScheduleBuilder
                .cronSchedule(sysJob.getCornCode());

        if(ObjectUtil.isEmpty(builder)){
            throw new RuntimeException("创建定时任务失败: 任务运行类型异常" );
        }

        //按新的cronExpression表达式构建一个新的trigger
        return TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(sysJob.getId()))
                .withSchedule(builder).build();
    }

    public static Trigger createDailyTimeIntervalScheduleJob(SysJob sysJob) {
        DailyTimeIntervalScheduleBuilder builder = DailyTimeIntervalScheduleBuilder
                .dailyTimeIntervalSchedule()
                .withIntervalInSeconds(sysJob.getIntervals());

        //设置开始时间
        LocalTime startTime = sysJob.getStartTime();
        if (ObjectUtil.isNotEmpty(startTime)){
            builder.startingDailyAt(TimeOfDay.hourMinuteAndSecondOfDay(startTime.getHour(), startTime.getMinute(),startTime.getSecond()));
        }

        //设置结束时间
        LocalTime endTime = sysJob.getEndTime();
        if (ObjectUtil.isNotEmpty(endTime)){
            builder.endingDailyAt(TimeOfDay.hourMinuteAndSecondOfDay(endTime.getHour(), endTime.getMinute(),endTime.getSecond()));
        }

        //设置周几运行
        if (ObjectUtil.isNotEmpty(sysJob.getWeekDay()) && !"8".equals(sysJob.getWeekDay())){
            Integer[] weekDay = (Integer[]) ConvertUtils.convert(sysJob.getWeekDay().split(","), Integer.class);
            builder.onDaysOfTheWeek(weekDay);
        }

        if(ObjectUtil.isEmpty(builder)){
            throw new RuntimeException("创建定时任务失败: 任务运行类型异常" );
        }

        //按新的cronExpression表达式构建一个新的trigger
        return TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(sysJob.getId()))
                .withSchedule(builder).build();
    }




    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, SysJob sysJob) {
        try {
        	//构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(getJobKey(sysJob.getId())).build();

            Trigger trigger = null;

            //按corn执行
            if (sysJob.getRunType().equals(1)){
                trigger = createCronScheduleJob(sysJob);
            }else {
                trigger = createDailyTimeIntervalScheduleJob(sysJob);
            }

            //放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(SysJob.JOB_PARAM_KEY, sysJob);

            scheduler.scheduleJob(jobDetail, trigger);

            //暂停任务
            if(sysJob.getState() == SysJob.RobotRuleState.STOP.getCode()){
            	pauseJob(scheduler, sysJob.getId());
            }
        } catch (SchedulerException e) {
            throw new RuntimeException("创建定时任务失败", e);
        }
    }
    
    /**
     * 更新定时任务
     */
    public static void updateScheduleJob(Scheduler scheduler, SysJob sysJob) {
        try {
            TriggerKey triggerKey = getTriggerKey(sysJob.getId());

            ScheduleBuilder scheduleBuilder = null;

            //按corn表达式执行
            if (sysJob.getRunType().equals(SysJob.RobotRuleRunType.CORN.getCode())){
                //表达式调度构建器
                scheduleBuilder = CronScheduleBuilder.cronSchedule(sysJob.getCornCode())
                        .withMisfireHandlingInstructionDoNothing();
            }

            //按秒执行
            if (sysJob.getRunType().equals(SysJob.RobotRuleRunType.SECONDS.getCode())){
                //表达式调度构建器
                scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(sysJob.getIntervals())//每隔Intervals秒执行一次
                        .repeatForever();
            }

            //按新的cronExpression表达式重新构建trigger
            Trigger trigger =  getTrigger(scheduler, sysJob.getId());

            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            //参数
            trigger.getJobDataMap().put(SysJob.JOB_PARAM_KEY, sysJob);
            
            scheduler.rescheduleJob(triggerKey, trigger);
            
            //暂停任务
            if(sysJob.getState() == SysJob.RobotRuleState.STOP.getCode()){
            	pauseJob(scheduler, sysJob.getId());
            }
            
        } catch (SchedulerException e) {
            throw new RuntimeException("更新定时任务失败", e);
        }
    }

    /**
     * 立即执行任务
     */
    public static void run(Scheduler scheduler, SysJob scheduleJob) {
        try {
        	//参数
        	JobDataMap dataMap = new JobDataMap();
        	dataMap.put(SysJob.JOB_PARAM_KEY, scheduleJob);
        	
            scheduler.triggerJob(getJobKey(scheduleJob.getId()), dataMap);
        } catch (SchedulerException e) {
            throw new RuntimeException("立即执行定时任务失败", e);
        }
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new RuntimeException("暂停定时任务失败", e);
        }
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new RuntimeException("暂停定时任务失败", e);
        }
    }

    /**
     * 删除定时任务
     */
    public static void deleteScheduleJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new RuntimeException("删除定时任务失败", e);
        }
    }
}
