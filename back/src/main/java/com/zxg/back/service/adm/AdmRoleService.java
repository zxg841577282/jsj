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
}
