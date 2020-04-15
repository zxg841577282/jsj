package com.zxg.back.web.controller;

//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import com.zxg.back.service.adm.AdmRoleService;
import com.zxg.back.service.adm.AdmUserService;
import com.zxg.back.web.resp.R;
import entity.AdmRole;
import entity.AdmUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AdmUserService admUserService;
    private final AdmRoleService admRoleService;

    @ApiOperation(value = "测试接口")
    @GetMapping("/testApi")
    public String queryBaseSeller(String password) {

        //密码加密
        String encode = bCryptPasswordEncoder.encode(password);

        return encode;
    }

    @ApiOperation(value = "登陆")
    @PostMapping("/login")
    public R login(String username, String password) {

        if(StringUtils.isEmpty(username)){ return R.error("用户名不能为空"); }
        if(StringUtils.isEmpty(password)){ return R.error("密码不能为空"); }

        // 查询用户是否存在 并返回用户数据
        try {
            AdmUser admUser = (AdmUser)admUserService.loadUserByUsername(username);
            if(StringUtils.isEmpty(admUser)){
                return R.error("用户名或密码错误");
            }else{

//                //获取角色列表，存入session
//                if(session.getAttribute("") == null){
//                    session.setAttribute(Const.ROLE,admUser.getRoles());
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("系统错误,请刷新");
        }
        return R.ok();
    }

    @ApiOperation(value = "测试是否登陆")
    @GetMapping("/testLoginSuccess")
    public String testLoginSuccess() {

        return "成功";
    }

}
