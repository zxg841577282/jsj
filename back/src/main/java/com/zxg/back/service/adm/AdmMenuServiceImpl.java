package com.zxg.back.service.adm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import entity.AdmMenu;
import entity.AdmRoleMenu;
import lombok.RequiredArgsConstructor;
import mapper.AdmMenuMapper;
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
public class AdmMenuServiceImpl implements AdmMenuService {
    private final AdmRoleMenuMapper admRoleMenuMapper;
    private final AdmMenuMapper admMenuMapper;


    @Override
    public List<AdmMenu> selectByRoleIds(List<Integer> roleIds) {

        List<AdmMenu> menus = new ArrayList<>();

        if (ListUtil.ListIsNull(roleIds)){
            QueryWrapper<AdmRoleMenu> qw = new QueryWrapper<>();
            qw.in("role_id",roleIds);
            List<AdmRoleMenu> admRoleMenus = admRoleMenuMapper.selectList(qw);

            if (ListUtil.ListIsNull(admRoleMenus)){
                List<Integer> menuIdList = admRoleMenus.stream().map(AdmRoleMenu::getMenuId).distinct().collect(Collectors.toList());

                menus = selectMenuList(menuIdList);
            }
        }

        return menus;
    }

    @Override
    public List<AdmMenu> selectMenuList(List<Integer> menuIds){

        List<AdmMenu> admMenus = new ArrayList<>();

        if (ListUtil.ListIsNull(menuIds)){
            QueryWrapper<AdmMenu> qw = new QueryWrapper<>();
            qw.in("id",menuIds);
            admMenus = admMenuMapper.selectList(qw);
        }

        return admMenus;
    }

}
