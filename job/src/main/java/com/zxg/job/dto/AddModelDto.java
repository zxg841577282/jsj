package com.zxg.job.dto;

import com.zxg.job.domain.dto.AddBaseJob;
import com.zxg.job.domain.dto.AddCornJob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddModelDto {
    //运行类型 0按intervals 每秒执行  1按corn表达式运行
    private Integer runType;

    //corn任务类
    private AddCornJob cornJob;

    //基础任务类
    private AddBaseJob baseJob;
}
