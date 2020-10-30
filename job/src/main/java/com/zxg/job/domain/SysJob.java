package com.zxg.job.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zxg.job.domain.dto.AddBaseJob;
import com.zxg.job.domain.dto.AddCornJob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysJob extends Model<SysJob>{

    /**
     * 任务调度参数key
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    @TableId(type = IdType.AUTO)
    private Long id;

    //类名
    private String beanName;
    
    //方法名
    private String methodName;

    //任务名称
    private String name;

    //运行类型 0按intervals 每秒执行  1按corn表达式运行
    private Integer runType;

    //间隔时间 单位秒
    private Integer intervals;

    //corn表达式
    private String cornCode;

    //开始时间  09:00:00
    private LocalTime startTime;

    //结束时间  19:00:00
    private LocalTime endTime;

    //每周几运行  1,2,3,4,5,6,7  8为每天
    private String weekDay;

    //创建时间
    private LocalDateTime createTime;

    //更新时间
    private LocalDateTime updateTime;

    //状态 0使用中 1已终止
    private Integer state;

    public SysJob(AddCornJob cornJob) {
        this.beanName = "MyJobTask";
        this.methodName = "JobOne";
        this.name = "Corn任务";
        this.runType = RobotRuleRunType.CORN.code;
        this.cornCode = cornJob.getCornJob();
        this.createTime = LocalDateTime.now();
        this.state = RobotRuleState.RUNNABLE.code;
    }

    public SysJob(AddBaseJob baseJob) {
        this.beanName = "MyJobTask";
        this.methodName = "JobOne";
        this.name = "Base任务";
        this.runType = RobotRuleRunType.SECONDS.code;
        this.intervals = baseJob.getIntervals();
        this.startTime = baseJob.getStartTime();
        this.endTime = baseJob.getEndTime();
        this.weekDay = baseJob.getWeekDay();
        this.createTime = LocalDateTime.now();
        this.state = RobotRuleState.RUNNABLE.code;
    }

    public enum RobotRuleRunType {
        SECONDS(0, "按秒执行"),
        CORN(1, "按corn执行")
        ;
        private int code;
        private String desc;

        RobotRuleRunType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static String getStateText(Integer state) {
            SysJob.RobotRuleRunType[] values = SysJob.RobotRuleRunType.values();
            for (SysJob.RobotRuleRunType var : values) {
                if (state == var.getCode()) {
                    return var.getDesc();
                }
            }
            return "";
        }
    }

    public enum RobotRuleState {
        RUNNABLE(0, "使用中"),
        STOP(1, "已终止")
        ;
        private int code;
        private String desc;

        RobotRuleState(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static String getStateText(Integer state) {
            SysJob.RobotRuleState[] values = SysJob.RobotRuleState.values();
            for (SysJob.RobotRuleState var : values) {
                if (state == var.getCode()) {
                    return var.getDesc();
                }
            }
            return "";
        }
    }

    @Override
    public String toString() {
        return "SysJob{" +
                "id=" + id +
                ", beanName='" + beanName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", name='" + name + '\'' +
                ", runType=" + runType +
                ", intervals=" + intervals +
                ", cornCode='" + cornCode + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", state=" + state +
                '}';
    }


}
