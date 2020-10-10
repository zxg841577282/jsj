package com.zxg.security.controller;

import cn.hutool.core.lang.Dict;
import com.zxg.security.rateLimiter.RateLimiter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import other.R;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/api/test")
@AllArgsConstructor
@Api(tags = "测试接口管理")
public class TestController {

    @RateLimiter(value = 1.0, timeout = 300)
    @ApiOperation(value = "test1")
    @GetMapping("/test1")
    public R test1() {
        log.info("【test1】被执行了。。。。。");
        Map<String,String> map = new HashMap<>();
        map.put("msg", "hello,world!");
        map.put("description", "别想一直看到我，不信你快速刷新看看~");
        return R.ok(map);
    }

    @GetMapping("/test2")
    @ApiOperation(value = "test2")
    public R test2() {
        log.info("【test2】被执行了。。。。。");
        Map<String,String> map = new HashMap<>();
        map.put("msg", "hello,world!");
        map.put("description", "我一直都在，卟离卟弃");
        return R.ok(map);
    }


}
