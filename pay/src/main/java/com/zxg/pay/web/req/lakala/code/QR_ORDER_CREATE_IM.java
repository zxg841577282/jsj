package com.zxg.pay.web.req.lakala.code;

import lombok.Setter;
import other.ResultException;

import java.math.BigDecimal;

/**
 * @Author: zhou_xg
 * @Date: 2020/3/30
 * @Purpose: 扫码支付动态二维码入参
 */
@Setter
public class QR_ORDER_CREATE_IM {
    private String shopNo;//商户号

    private String termId;//终端号

    private String shopName;//商户名称

    private BigDecimal amount;//金额

    private String orderId;//订单号

    private String subject;//订单标题

    private String description;//订单描述

    public String getShopNo() {
        if (shopNo==null){throw new ResultException("扫码支付异常：商户号不能为空");}
        return shopNo;
    }

    public String getTermId() {
        if (termId==null){throw new ResultException("扫码支付异常：终端号不能为空");}
        return termId;
    }

    public String getShopName() {
        return shopName=shopName==null?"":shopName;
    }

    public BigDecimal getAmount() {
        if (amount==null){throw new ResultException("扫码支付异常：订单金额不能为空");}
        return amount;
    }

    public String getOrderId() {
        if (orderId==null){throw new ResultException("扫码支付异常：订单号不能为空");}
        return orderId;
    }

    public String getSubject() {
        return subject=subject==null?"":subject;
    }

    public String getDescription() {
        return description=description==null?"":description;
    }

    public QR_ORDER_CREATE_IM(String shopNo, String termId, String shopName, BigDecimal amount, String orderId) {
        this.shopNo = shopNo;
        this.termId = termId;
        this.shopName = shopName;
        this.amount = amount;
        this.orderId = orderId;
        this.subject = "";
        this.description = "";
    }
}
