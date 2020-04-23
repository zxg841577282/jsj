package com.zxg.get_logistics.web.req.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/20 0020
 * @Purpose:
 */
@Data
public class WXAddOrderIM {
    @ApiModelProperty(value = "订单号", dataType = "String")
    @NotNull(message = "订单号不能为NULL")
    private String order_id;

    @ApiModelProperty(value = "openId", dataType = "String")
    @NotNull(message = "用户openId不能为NULL")
    private String openid;

    @ApiModelProperty(value = "接口调用凭证", dataType = "String")
    @NotNull(message = "接口调用凭证不能为NULL")
    private String access_token;

    @ApiModelProperty(value = "快递客户编码或者现付编码", dataType = "String")
    @NotNull(message = "快递客户编码或者现付编码不能为NULL")
    private String biz_id;

    @ApiModelProperty(value = "快递公司ID", dataType = "String")
    @NotNull(message = "快递公司ID不能为NULL")
    private String delivery_id;

    @ApiModelProperty(value = "快递备注信息", dataType = "String")
    private String custom_remark;
}
