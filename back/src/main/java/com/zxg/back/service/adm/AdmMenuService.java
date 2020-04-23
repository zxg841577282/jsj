package com.zxg.back.service.adm;

import entity.AdmMenu;

import java.util.List;

public interface AdmMenuService {
    /**
     * 角色查所拥有权限
     * @param roleIds 角色ID集合
     * @return
     */
    List<AdmMenu> selectByRoleIds(List<Integer> roleIds);

    /**
     * 权限ID获取列表
     * @param menuIds 权限ID集合
     * @return
     */
    List<AdmMenu> selectMenuList(List<Integer> menuIds);
}
