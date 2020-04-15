package com.zxg.back.service.adm;

import entity.AdmRole;
import entity.AdmUser;
import lombok.RequiredArgsConstructor;
import mapper.AdmUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/15
 * @Purpose:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdmUserServiceImpl implements AdmUserService {
    private final AdmUserMapper admUserMapper;
    private final AdmRoleService admRoleService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //获取当前账户
        AdmUser admUser = admUserMapper.loadUserByUsername(s);

        //获取当前账户所属角色
        List<AdmRole> roleList = admRoleService.selectByUserId(admUser.getId());
        admUser.setRoles(roleList);

        return admUser;
    }
}
