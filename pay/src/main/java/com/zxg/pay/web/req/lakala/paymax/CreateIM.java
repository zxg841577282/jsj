package com.zxg.pay.web.req.lakala.paymax;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/23
 * @Purpose: 创建充值订单
 */
@Getter
@Setter
public class CreateIM {

    @NotNull(message = "应用的appKey不能为空")
    private String appKey;

    @NotNull(message = "订单金额不能为空")
    private BigDecimal amount;

    //商品标题
    private String subject;

    //商品信息
    private String body;

    //客户端IP
    private String client_ip;

    //订单号
    @NotNull(message = "订单号不能为空")
    private String orderNo;

    //订单备注
    private String description;

    //系统中用户标识
    @NotNull(message = "系统中用户标识不能为空")
    private String userId;

    //回调地址
    @NotNull(message = "回调地址不能为空")
    private String return_url;


    public CreateIM(BigDecimal amount,  String orderNo, String userId,String client_ip) {
        this.appKey = "app_7hqF2S6GYXET457i";
        this.amount = amount;
        this.subject = "商品标题";
        this.body = "商品信息";
        this.client_ip = client_ip;
        this.orderNo = orderNo;
        this.description = "";
        this.userId = userId;
        this.return_url = "http://132.123.221.22/333/kad";
    }
}
