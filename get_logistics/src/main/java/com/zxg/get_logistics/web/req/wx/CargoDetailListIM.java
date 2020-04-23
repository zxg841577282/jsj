package com.zxg.get_logistics.web.req.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/21 0021
 * @Purpose:
 */
@Data
public class CargoDetailListIM {
    @ApiModelProperty(value = "商品名", dataType = "String")
    @NotNull(message = "商品名不能为NULL")
    private String name;

    @ApiModelProperty(value = "商品数量", dataType = "String")
    @NotNull(message = "商品数量不能为NULL")
    private Integer count;
}
