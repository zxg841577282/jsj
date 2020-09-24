package com.zxg.actuator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @GetMapping("/getMsg")
    public String getMsg(){
        return "asdasda";
    }

}
