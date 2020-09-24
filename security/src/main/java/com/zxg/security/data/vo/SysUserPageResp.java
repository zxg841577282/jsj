package com.zxg.security.data.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "用户列表",description = "用户列表色")
public class SysUserPageResp {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("用户名")
    private String name;

}
