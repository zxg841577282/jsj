package com.zxg.back.service.adm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import entity.AdmMenu;
import entity.AdmRole;
import entity.AdmRoleMenu;
import entity.AdmUserRole;
import lombok.RequiredArgsConstructor;
import mapper.AdmRoleMapper;
import mapper.AdmRoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/15
 * @Purpose:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdmRoleServiceImpl implements AdmRoleService {
    private final AdmRoleMapper admRoleMapper;
    private final AdmUserRoleService admUserRoleService;
    private final AdmMenuService admMenuService;
    private final AdmRoleMenuService admRoleMenuService;

    @Override
    public List<AdmRole> selectByUserId(Integer userId) {

        List<AdmUserRole> list = admUserRoleService.selectByUserId(userId);

        List<AdmRole> roleList = new ArrayList<>();

        if (ListUtil.ListIsNull(list)) {
            List<Integer> roleIds = list.stream().map(AdmUserRole::getRoleId).collect(Collectors.toList());

            roleList = selectByRoleIds(roleIds);
        }

        return roleList;
    }

    @Override
    public List<AdmRole> selectByRoleIds(List<Integer> roleIds) {

        List<AdmRole> backList = new ArrayList<>();

        if (ListUtil.ListIsNull(roleIds)) {
            QueryWrapper<AdmRole> qw = new QueryWrapper<>();
            qw.in("id", roleIds);
            backList = admRoleMapper.selectList(qw);

            List<AdmRoleMenu> admRoleMenus = admRoleMenuService.selectByRoleIds(roleIds);

            if (ListUtil.ListIsNull(admRoleMenus)) {
                List<Integer> menuIds = admRoleMenus.stream().map(AdmRoleMenu::getMenuId).distinct().collect(Collectors.toList());

                List<AdmMenu> menus = admMenuService.selectMenuList(menuIds);

                backList.forEach(
                        s -> {
                            List<Integer> myMenuIds = admRoleMenus.stream().filter(p -> p.getRoleId().equals(s.getId())).map(AdmRoleMenu::getMenuId).distinct().collect(Collectors.toList());

                            s.setMenuList(menus.stream().filter(p -> myMenuIds.contains(p.getId())).collect(Collectors.toList()));
                        }
                );
            }
        }

        return backList;
    }
}
