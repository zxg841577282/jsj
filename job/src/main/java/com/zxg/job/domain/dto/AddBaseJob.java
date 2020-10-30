package com.zxg.job.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class AddBaseJob {
    //间隔时间 单位秒
    private Integer intervals;

    //开始时间  09:00:00
    private LocalTime startTime;

    //结束时间  19:00:00
    private LocalTime endTime;

    //每周几运行  1,2,3,4,5,6,7  8为每天
    private String weekDay;
}
