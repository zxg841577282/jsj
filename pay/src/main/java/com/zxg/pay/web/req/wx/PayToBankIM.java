package com.zxg.pay.web.req.wx;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/22
 * @Purpose: 企业支付到银行卡
 */
@Getter
@Setter
public class PayToBankIM extends BaseWxInfoIM{

    @NotNull(message = "订单号不能为NULL")
    private String orderNo;

    @NotNull(message = "支付金额不能为NULL，单位为分")
    private BigDecimal totalFee;

    //付款说明
    private String desc;

    @NotNull(message = "收款方银行卡号不能为NULL")
    private String bankNo;

    @NotNull(message = "收款方用户名不能为NULL")
    private String bankUser;

    @NotNull(message = "银行卡所在开户行编号不能为NULL")
    private String bankCode;

    public PayToBankIM(String appid, String mch_id, String mchidKey,  String orderNo, BigDecimal totalFee, String desc, String bankNo, String bankUser, String bankCode) {
        super(appid, mch_id, mchidKey);
        this.orderNo = orderNo;
        this.totalFee = totalFee;
        this.desc = (desc==null?"":desc);
        this.bankNo = bankNo;
        this.bankUser = bankUser;
        this.bankCode = bankCode;
    }
}
