package com.zxg.security.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysUserPageReq extends BasePageDTO {

    @ApiModelProperty("用户登录名")
    private String account;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("用户状态 0禁用 1启用")
    private Integer status;

}
