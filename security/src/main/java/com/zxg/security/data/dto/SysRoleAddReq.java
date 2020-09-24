package com.zxg.security.data.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ApiModel(value = "新增|编辑后台角色",description = "新增|编辑后台角色")
public class SysRoleAddReq {

    @ApiModelProperty("ID 编辑不用传")
    @NotNull(groups = Update.class,message = "部门ID不能为空")
    private Integer id;

    @ApiModelProperty("角色名称")
    @NotNull(message = "角色名称不能为空")
    private String name;

    @ApiModelProperty("权限列表ID")
    @Size(min = 1,message = "权限列表ID不能为空")
    @NotNull(message = "权限列表ID不能为空")
    private List<Long> permissionIds;

}
