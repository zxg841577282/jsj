package com.zxg.back.service.adm;

import entity.AdmRole;

import java.util.List;

public interface AdmRoleService {
    /**
     * 账户ID查询所属角色
     * @param userId 账户ID
     * @return
     */
    List<AdmRole> selectByUserId(Integer userId);

    /**
     * 角色ID查询角色列表
     * @param roleIds 角色ID集合
     * @return
     */
    List<AdmRole> selectByRoleIds(List<Integer> roleIds);
}
