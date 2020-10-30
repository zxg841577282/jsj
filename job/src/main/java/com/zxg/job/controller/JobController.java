package com.zxg.job.controller;

import com.zxg.job.domain.SysJob;
import com.zxg.job.dto.AddModelDto;
import com.zxg.job.dto.UpdateModelDto;
import com.zxg.job.service.ScheduleJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JobController {
    @Autowired
    private ScheduleJobService scheduleJobService;


    @GetMapping(value = "/getList")
    public List<SysJob> getList(){
        return new SysJob().selectAll();
    }

    @PostMapping(value = "/addModel")
    public boolean addModel(@RequestBody AddModelDto dto){
        return scheduleJobService.addModel(dto);
    }

    @PostMapping(value = "/updateModel")
    public boolean updateModel(@RequestBody UpdateModelDto dto){
        return scheduleJobService.updateModel();
    }

}
