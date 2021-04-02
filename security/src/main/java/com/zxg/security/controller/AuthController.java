package com.zxg.security.controller;

import com.zxg.security.data.dto.UpdateLoginPwdDTO;
import com.zxg.security.data.entity.SysUser;
import com.zxg.security.data.service.SysRoleService;
import com.zxg.security.data.service.SysUserService;
import com.zxg.security.data.vo.AdminLoginUserResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import other.R;

import javax.validation.Valid;
import java.util.ArrayList;


@RestController
@Log4j2
@RequestMapping("/api/auth")
@AllArgsConstructor
@Api(tags = "登陆用户管理")
public class AuthController {
    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;

    @GetMapping("/getLoginUserInfo")
    @ApiOperation(value = "获取当前登陆用户信息")
    public R getLoginUserInfo() {

        //获取当前登陆账户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AdminLoginUserResp sysUser = (AdminLoginUserResp)authentication.getPrincipal();

        return R.ok(sysUser);
    }

    @PutMapping("/updateLoginUserPwd")
    @ApiOperation(value = "修改当前登陆用户密码")
    public R updateLoginUserPwd(@RequestBody UpdateLoginPwdDTO req) {

        //获取当前登陆账户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AdminLoginUserResp sysUser = (AdminLoginUserResp)authentication.getPrincipal();

        SysUser user = sysUserService.selectById(sysUser.getId());

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (user.getPwd().equals(bCryptPasswordEncoder.encode(req.getOldPwd()))){
            user.setPwd(bCryptPasswordEncoder.encode(req.getNewPwd()));
            sysUserService.update(user);
        }

        return R.error("原密码不正确");
    }

    @GetMapping("/getMyPermission")
    @ApiOperation(value = "查询可用权限")
    public R getMyPermission() {

        //获取当前登陆账户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //匿名用户没有权限
        if ("anonymousUser".equals(authentication.getPrincipal())){
            return R.error("匿名账户暂无权限");
        }

        if (authentication.getPrincipal() instanceof AdminLoginUserResp) {
            AdminLoginUserResp sysUser = (AdminLoginUserResp)authentication.getPrincipal();

            return R.ok(sysRoleService.getPerList(sysUser.getRoleId()));
        }

        return R.ok(new ArrayList<>());
    }

}
