package com.zxg.security.data.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("新增后台账户")
public class SysUserAddReq {

    @ApiModelProperty("用户名")
    @NotNull(message = "用户名不能为空")
    private String name;

    @ApiModelProperty("登陆账户名")
    @NotNull(message = "登陆账户名不能为空")
    private String account;

    @ApiModelProperty("密码")
    @NotNull(message = "密码不能为空")
    private String pwd;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态 0禁用 1启用")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @ApiModelProperty("角色")
    @NotNull(message = "角色不能为空")
    private Long roleId;

}
