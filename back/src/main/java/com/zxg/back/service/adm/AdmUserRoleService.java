package com.zxg.back.service.adm;

import entity.AdmUserRole;

import java.util.List;

public interface AdmUserRoleService {
    /**
     * 账户查询所属角色
     *
     * @param userId 账户ID
     * @return
     */
    List<AdmUserRole> selectByUserId(Integer userId);
}
