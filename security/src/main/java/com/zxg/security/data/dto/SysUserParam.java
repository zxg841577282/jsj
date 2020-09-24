package com.zxg.security.data.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户修改参数<p>
 *
 * @author hepeiyun
 * @since 2019/11/4
 */
@Data
@ApiModel(value = "用户操作实体类")
public class SysUserParam {

    private Integer id;

    private String password;

    @ApiModelProperty(value = "用户名称")
    private String name;

    @ApiModelProperty(value = "用户登录名")
    private String account;

    @ApiModelProperty(value = "备注")
    private String remark;


}
