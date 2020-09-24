package com.zxg.security.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxg.security.data.dto.SysUserAddReq;
import com.zxg.security.data.dto.SysUserPageReq;
import com.zxg.security.data.dto.SysUserUpdateReq;
import com.zxg.security.data.entity.SysUser;
import com.zxg.security.data.service.SysUserService;
import com.zxg.security.data.vo.SysUserPageResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import other.R;

import java.util.List;


@RestController
@Log4j2
@RequestMapping("/api/sys_user")
@AllArgsConstructor
@Api(tags = "后台账户管理")
@PreAuthorize("hasAuthority('sysuser')")
public class SysUserController {
    private final SysUserService sysUserService;

    @PostMapping("/getPageList")
    @ApiOperation(value = "获取分页列表",response = SysUserPageResp.class)
    @PreAuthorize("hasAuthority('menu_sysuser_list')")
    public R getPageList(@RequestBody SysUserPageReq req) {
        return R.ok(sysUserService.getPageList(req));
    }

    @PostMapping("/addModel")
    @ApiOperation(value = "新增")
    @PreAuthorize("hasAuthority('but_menu_sysuser_list_add')")
    public R addModel(@RequestBody SysUserAddReq req) {
        return R.status(sysUserService.addModel(req,new BCryptPasswordEncoder().encode(req.getPwd())));
    }

    @PutMapping("/updateModel")
    @ApiOperation(value = "编辑")
    @PreAuthorize("hasAuthority('but_menu_sysuser_list_update')")
    public R updateModel(@RequestBody SysUserUpdateReq req) {
        return R.status(sysUserService.updateModel(req));
    }

    @DeleteMapping("/deleteModel")
    @ApiOperation(value = "删除")
    @PreAuthorize("hasAuthority('but_menu_sysuser_list_delete')")
    public R deleteModel(@RequestBody List<Long> req) {
        return R.status(new SysUser().delete(new LambdaQueryWrapper<SysUser>().in(SysUser::getId, req)));
    }

    @GetMapping("/resetPwd/{id}")
    @ApiOperation(value = "重置密码为123456")
    @PreAuthorize("hasAuthority('but_menu_sysuser_list_resetPwd')")
    public R resetPwd(@PathVariable Long id) {
        return R.status(sysUserService.resetPwd(id,new BCryptPasswordEncoder().encode("123456")));
    }
}
