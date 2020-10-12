package com.zxg.jwt_login.web;

import cn.hutool.core.util.ObjectUtil;
import com.zxg.jwt_login.common.handler.LoginUser;
import com.zxg.jwt_login.data.domain.AdmUser;
import com.zxg.jwt_login.data.dto.LoginDTO;
import com.zxg.jwt_login.common.jwt.JwtUtil;
import com.zxg.jwt_login.service.AdmUserService;
import com.zxg.jwt_login.common.util.bcrypt.BCryptPasswordEncoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import other.R;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "auth")
public class AuthController {
    @Autowired
    private AdmUserService admUserService;


    @ApiOperation(value = "登陆")
    @PostMapping("/login")
    public R login(@RequestBody LoginDTO dto){
        AdmUser admUser = admUserService.getByUsername(dto.getUsername());

        if (ObjectUtil.isEmpty(admUser)){
            return R.error("账号不存在");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(dto.getPassword(), admUser.getPassword())) {
            return R.error("账号密码正确");
        }

        // 更新登录情况
        admUser.setUpdateTime(LocalDateTime.now());
        if (!admUserService.updateById(admUser)) {
            return R.error("更新数据失败");
        }

        // token
        String token = new JwtUtil().createToken(admUser.getId());

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", token);
        return R.ok(result);
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getInfo")
    public R getInfo(@LoginUser @ApiParam(hidden = true) Integer userId){
        return R.ok(admUserService.getById(userId));
    }

}
