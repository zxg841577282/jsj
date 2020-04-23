package com.zxg.get_logistics.web.req.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/21 0021
 * @Purpose: 商品信息，会展示到物流通知消息中shop
 */
@Data
public class ShopIM {
    @ApiModelProperty(value = "商家小程序的路径，建议为订单页面", dataType = "String")
    @NotNull(message = "商家小程序的路径不能为NULL")
    private String wxa_path;

    @ApiModelProperty(value = "商品缩略图 url", dataType = "String")
    @NotNull(message = "商品缩略图 url不能为NULL")
    private String img_url;

    @ApiModelProperty(value = "商品名称", dataType = "String")
    @NotNull(message = "商品名称不能为NULL")
    private String goods_name;

    @ApiModelProperty(value = "商品数量", dataType = "String")
    @NotNull(message = "商品数量不能为NULL")
    private Integer goods_count;
}
