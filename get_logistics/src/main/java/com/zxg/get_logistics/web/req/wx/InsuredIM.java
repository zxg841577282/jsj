package com.zxg.get_logistics.web.req.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/21 0021
 * @Purpose: 保价信息
 */
@Data
public class InsuredIM {
    @ApiModelProperty(value = "是否保价，0 表示不保价，1 表示保价", dataType = "String")
    @NotNull(message = "是否保价不能为NULL")
    private Integer use_insured;

    @ApiModelProperty(value = "保价金额，单位是分，比如: 10000 表示 100 元", dataType = "String")
    @NotNull(message = "保价金额不能为NULL")
    private Integer insured_value;
}
