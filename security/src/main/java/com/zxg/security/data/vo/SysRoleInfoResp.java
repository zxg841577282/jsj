package com.zxg.security.data.vo;

import com.zxg.security.data.entity.SysPermission;
import com.zxg.security.data.entity.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("角色详情")
public class SysRoleInfoResp extends SysRole {

    @ApiModelProperty("权限列表")
    private List<SysPermission> perList = new ArrayList<>();
}
