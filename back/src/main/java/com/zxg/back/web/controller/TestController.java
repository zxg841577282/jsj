package com.zxg.back.web.controller;

import com.alibaba.fastjson.JSON;
import com.zxg.back.service.adm.AdmUserService;
import com.zxg.back.web.req.adm.AdmPageListIM;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/14
 * @Purpose:
 */
@RestController
@RequestMapping("/back/test")
@Api(description = "测试")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {
    private final AdmUserService admUserService;

    @Resource
    private SessionRegistry sessionRegistry;


    @PreAuthorize("hasAuthority('one')")
    @ApiOperation(value = "测试权限")
    @GetMapping("/testHasPermission")
    public Boolean testHasPermission() {
        return true;
    }

    @PreAuthorize("hasAuthority('two2222')")
    @ApiOperation(value = "测试权限2")
    @GetMapping("/testHasPermission2")
    public Boolean testHasPermission2() {
        return true;
    }

    @ApiOperation(value = "测试是否登陆")
    @GetMapping("/testLoginSuccess")
    public String testLoginSuccess() {

        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<SessionInformation> allSessions = sessionRegistry.getAllSessions(authentication.getPrincipal(), false);

        return JSON.toJSONString(allPrincipals);
    }

    @ApiOperation(value = "分页测试")
    @PostMapping("/testPageList")
    public Object testPageList(@RequestBody AdmPageListIM im) {
        return im.getPageList();
    }

    @ApiOperation(value = "test")
    @PostMapping("/test")
    public Object test(Integer id) {
        return admUserService.getById(id);
    }


}
