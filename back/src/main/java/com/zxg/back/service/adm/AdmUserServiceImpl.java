package com.zxg.back.service.adm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import entity.AdmMenu;
import entity.AdmRole;
import entity.AdmUser;
import lombok.RequiredArgsConstructor;
import mapper.AdmUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import util.ListUtil;

import java.util.ArrayList;
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

        List<AdmRole> admRoles = admRoleService.selectByUserId(admUser.getId());

        //获取当前账户所属角色
        admUser.setRoles(admRoles);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if (ListUtil.ListIsNull(admRoles)){

            for (AdmRole admRole : admRoles) {
                for (AdmMenu menu : admRole.getMenuList()) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(menu.getCode());
                    //此处将权限信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
                    grantedAuthorities.add(grantedAuthority);
                }

            }
        }

        return new User(admUser.getUsername(),admUser.getPassword(),grantedAuthorities);
    }

    @Override
    public IPage<AdmUser> selectPageList(Page page) {
        return admUserMapper.selectPage(page,null);
    }
}
