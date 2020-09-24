package com.zxg.security.data.vo;

import com.zxg.security.data.entity.SysPermission;
import com.zxg.security.data.entity.SysRole;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SysRolePageResp extends SysRole {

    private List<SysPermission> permissionList;
}
