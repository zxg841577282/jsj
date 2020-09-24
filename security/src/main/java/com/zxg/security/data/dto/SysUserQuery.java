package com.zxg.security.data.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户搜索条件<p>
 *
 * @author hepeiyun
 * @since 2019/11/4
 */
@Data
@ApiModel("用户搜索条件")
public class SysUserQuery {

    @ApiModelProperty("用户登录名")
    private String account;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("是否是管理员 0不是 1是")
    private Integer admin;

    @ApiModelProperty("用户状态 0禁用 1启用")
    private Integer status;
}
