package com.zxg.security.data.service.impl;

import com.zxg.security.data.entity.SysRolePermission;
import com.zxg.security.data.service.SysRolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SysRolePermissionServiceImpl implements SysRolePermissionService {

    @Override
    public void insertByList(Long roleId, List<Long> permissionIds) {
        SysRolePermission srp;
        for (Long permissionId : permissionIds) {
            srp = new SysRolePermission(roleId,permissionId);
            srp.insert();
        }
    }
}
