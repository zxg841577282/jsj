package com.zxg.pay.web.req.wx;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/21
 * @Purpose: 企业支付到零钱
 */
@Getter
@Setter
public class PayToCashIM extends BaseWxInfoIM {

    @NotNull(message = "订单号不能为NULL")
    private String partner_trade_no;

    @NotNull(message = "用户OPENID不能为NULL")
    private String openid;

    @NotNull(message = "支付金额不能为NULL，单位为分")
    private BigDecimal amount;

    private String desc;

    private String spbill_create_ip;

    public PayToCashIM(String appid, String mch_id, String mchidKey, String partner_trade_no, String openid, BigDecimal amount, String desc, String spbill_create_ip) {
        super(appid, mch_id, mchidKey);
        this.partner_trade_no = partner_trade_no;
        this.openid = openid;
        this.amount = amount;
        this.desc = (desc == null ? "备注信息" : desc);
        this.spbill_create_ip = spbill_create_ip == null ? "" : spbill_create_ip;
    }
}
