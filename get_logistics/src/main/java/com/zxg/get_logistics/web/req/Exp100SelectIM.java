package com.zxg.get_logistics.web.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import util.AssertUtils;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/20
 * @Purpose: 物流100 物流轨迹查询
 */

@Getter
@Setter
public class Exp100SelectIM {

    @ApiModelProperty("发货人,必传")
    private String customer;//发货人

    @ApiModelProperty("密钥,必传")
    private String key;//密钥

    @ApiModelProperty("查询的快递公司的编码,必传")
    private String com;//查询的快递公司的编码

    @ApiModelProperty("单号,必传")
    private String num;//单号

    @ApiModelProperty("寄件人手机号")
    private String phone;//寄件人手机号

    @ApiModelProperty("出发地城市")
    private String from;//出发地城市

    @ApiModelProperty("目的地城市")
    private String to;//目的地城市

    @ApiModelProperty("添加此字段表示开通行政区域解析功能")
    private Integer resultv2;//添加此字段表示开通行政区域解析功能


    public String getCustomer() {
        AssertUtils.isNull(customer,"发货人不能为空");
        return customer;
    }

    public String getKey() {
        AssertUtils.isNull(key,"密钥不能为空");
        return key;
    }

    public String getCom() {
        AssertUtils.isNull(com,"快递公司的编码不能为空");
        return com;
    }

    public String getNum() {
        AssertUtils.isNull(num,"单号不能为空");
        return num;
    }
}
