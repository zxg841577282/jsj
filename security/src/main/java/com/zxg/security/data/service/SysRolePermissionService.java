package com.zxg.security.data.service;

import java.util.List;

public interface SysRolePermissionService {
    void insertByList(Long roleId, List<Long> permissionIds);
}
