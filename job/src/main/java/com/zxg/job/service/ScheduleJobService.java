package com.zxg.job.service;

import com.zxg.job.dto.AddModelDto;

public interface ScheduleJobService {
    boolean addModel(AddModelDto dto);

    boolean updateModel();

}
