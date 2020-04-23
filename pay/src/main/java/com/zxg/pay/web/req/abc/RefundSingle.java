package com.zxg.pay.web.req.abc;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author: zhou_xg
 * @Date: 2020/3/12 15:04
 * @Purpose:
 */
@Data
@NoArgsConstructor
public class RefundSingle {
    @NotNull(message = "订单日期不能为空")
    private String OrderDate;//必需 订单日期 YYYY/MM/DD
    @NotNull(message = "订单时间不能为空")
    private String OrderTime;//必需 订单时间 HH:MM:SS
    private String MerRefundAccountNo;//商户退款账号
    private String MerRefundAccountName;//商户退款名
    @NotNull(message = "支付订单号不能为空")
    private String OrderNo;//必需 支付订单号
    @NotNull(message = "退款交易编号不能为空")
    private String NewOrderNo;//必需 退款交易编号
    private String CurrencyCode;//必需 交易币种 156:人民币
    @NotNull(message = "退款金额不能为空")
    private String TrxAmount;//必需 退款金额
    private String MerchantRemarks;//附言
    /**
     * 0:普通退款
     * 1:维权退款
     */
    private String RefundType;//维权退款标志
    /**
     * 1：担保账簿
     * 2:二级商户账簿
     * 3：退款账簿
     */
    private String MerRefundAccountFlag;//平台商户担保支付退款资金来源


    public RefundSingle(String orderDate, String orderTime, String orderNo, String newOrderNo, String trxAmount) {
        OrderDate = orderDate;
        OrderTime = orderTime;
        MerRefundAccountNo = "";
        MerRefundAccountName = "";
        OrderNo = orderNo;
        NewOrderNo = newOrderNo;
        CurrencyCode = "156";
        TrxAmount = trxAmount;
        MerchantRemarks = "";
        RefundType = "";
        MerRefundAccountFlag = "";
    }

}
