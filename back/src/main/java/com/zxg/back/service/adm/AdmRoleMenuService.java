package com.zxg.back.service.adm;

import entity.AdmRoleMenu;

import java.util.List;

public interface AdmRoleMenuService {
    /**
     * 查询角色所拥有的权限
     *
     * @param roleIdList
     * @return
     */
    List<AdmRoleMenu> selectByRoleIds(List<Integer> roleIdList);
}
