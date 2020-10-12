package com.zxg.jwt_login.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxg.jwt_login.data.domain.AdmUser;
import org.springframework.stereotype.Service;

@Service
public class AdmUserService {

    public AdmUser getByUsername(String username){
        return new AdmUser().selectOne(new LambdaQueryWrapper<AdmUser>().eq(AdmUser::getUsername,username));
    }

    public boolean updateById(AdmUser admUser) {
        return admUser.updateById();
    }

    public AdmUser getById(Integer userId) {
        if (ObjectUtil.isEmpty(userId)){
            throw new RuntimeException("账户未登陆");
        }
        AdmUser admUser = new AdmUser().selectById(userId);
        if (ObjectUtil.isEmpty(admUser)){
            throw new RuntimeException("账户不存在");
        }
        return admUser;
    }
}
