package com.zxg.pay.web.req.lakala.code;

import lombok.Getter;
import lombok.Setter;
import other.ResultException;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: zhou_xg
 * @Date: 2020/3/30
 * @Purpose: 扫码退款动态二维码入参
 */
@Setter
@Getter
public class QR_REFUND_ORDER_IM {

    @NotNull(message = "扫码退款异常：商户号不能为空")
    private String shopNo;//商户号

    @NotNull(message = "扫码退款异常：终端号不能为空")
    private String termId;//终端号

    @NotNull(message = "扫码退款异常：原交易日期不能为空")
    private Date oriDate;//原交易日期

    @NotNull(message = "扫码退款异常：退款金额不能为空")
    private BigDecimal amount;//退款金额  单位分

    @NotNull(message = "扫码退款异常：平台交易单号不能为空")
    private String tradeNo;//平台交易单号

    @NotNull(message = "扫码退款异常：拉卡拉商户订单号不能为空")
    private String lklOrderNo;//拉卡拉商户订单号

    @NotNull(message = "扫码退款异常：退款订单号不能为空")
    private String refundOrderId;//商户退款单号

    public QR_REFUND_ORDER_IM(String shopNo, String termId, Date oriDate, BigDecimal amount, String tradeNo, String lklOrderNo, String refundOrderId) {
        this.shopNo = shopNo;
        this.termId = termId;
        this.oriDate = oriDate;
        this.amount = amount;
        this.tradeNo = tradeNo;
        this.lklOrderNo = lklOrderNo;
        this.refundOrderId = refundOrderId;
    }
}
