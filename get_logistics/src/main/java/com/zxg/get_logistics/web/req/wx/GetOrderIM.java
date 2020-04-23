package com.zxg.get_logistics.web.req.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/21 0021
 * @Purpose: 获取运单数据
 */
@Data
public class GetOrderIM {
    @ApiModelProperty(value = "接口调用凭证", dataType = "String")
    @NotNull(message = "接口调用凭证不能为NULL")
    private String access_token;

    @ApiModelProperty(value = "订单 ID，需保证全局唯一", dataType = "String")
    @NotNull(message = "订单 ID不能为NULL")
    private String order_id;

    @ApiModelProperty(value = "用户openid", dataType = "String")
    private String openid;

    @ApiModelProperty(value = "快递公司ID", dataType = "String")
    @NotNull(message = "快递公司ID不能为NULL")
    private String delivery_id;

    @ApiModelProperty(value = "运单ID", dataType = "String")
    @NotNull(message = "运单ID不能为NULL")
    private String waybill_id;
}
