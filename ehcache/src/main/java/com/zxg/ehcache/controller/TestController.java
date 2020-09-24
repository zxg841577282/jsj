package com.zxg.ehcache.controller;

import com.zxg.ehcache.entity.User;
import com.zxg.ehcache.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {
    @Autowired
    private UserService userService;

    @GetMapping("/getMsg")
    public void getInfo(Long id){
        User info = userService.getInfo(id);
        log.info("【user1】= {}", info);

        User info2 = userService.getInfo(id);
        log.info("【user2】= {}", info2);
    }

    @GetMapping("/save")
    public void save(User user){
        userService.save(user);
    }

    @GetMapping("/delete")
    public void delete(Long id){
        userService.delete(id);
    }

}
