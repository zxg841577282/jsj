package com.zxg.jwt_login.web;

import com.zxg.jwt_login.task.MyTask1;
import com.zxg.jwt_login.task.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import other.R;

@RestController
@RequestMapping(value = "/myTask1")
public class MyTask1Controller {
    @Autowired
    private TaskService taskService;

    @ApiOperation(value = "getList")
    @GetMapping("/getList")
    public R getList(){
        return R.ok(taskService.toArray());
    }

    @ApiOperation(value = "add")
    @GetMapping("/add")
    public R test(Integer name){
        return R.ok(taskService.addTask(new MyTask1("任务" + name,100 * 1000)));
    }

    @ApiOperation(value = "remove")
    @GetMapping("/remove")
    public R remove(Integer i){
        return R.ok(taskService.removeTask(new MyTask1("任务"+i)));
    }

    @ApiOperation(value = "clear")
    @GetMapping("/clear")
    public R clear(){
        taskService.clear();
        return R.ok();
    }
}
