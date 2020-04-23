package com.zxg.pay.web.req.wx;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/21
 * @Purpose: 微信申请退款
 */
@Getter
@Setter
public class RefundIM extends BaseWxInfoIM{

    @NotNull(message = "原系统订单号不能为NULL")
    private String out_trade_no;

    @NotNull(message = "退款单号不能为NULL")
    private String out_refund_no;

    @NotNull(message = "订单金额不能为NULL，单位为分")
    private BigDecimal totalFee;

    @NotNull(message = "订单金额不能为NULL，单位为分")
    private BigDecimal refundFee;

    //退款描述
    private String refund_desc;

    @NotNull(message = "退款回调地址不能为NULL")
    private String notify_url;

    //默认回调地址
    private static String defaultNotifyUrl = "http://jielong.siginfo.cn:8888/jielong/pay/asyncNoticeWxPay";

    public RefundIM(String appid, String mch_id, String mchidKey, String out_trade_no, String out_refund_no, BigDecimal totalFee, BigDecimal refundFee,String refund_desc,String notify_url) {
        super(appid, mch_id, mchidKey);
        this.out_trade_no = out_trade_no;
        this.out_refund_no = out_refund_no;
        this.totalFee = totalFee;
        this.refundFee = refundFee;
        this.refund_desc = (refund_desc==null?"":refund_desc);
        this.notify_url = (StringUtils.isEmpty(notify_url)?defaultNotifyUrl:notify_url);
    }
}
