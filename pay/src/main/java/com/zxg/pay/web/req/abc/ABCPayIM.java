package com.zxg.pay.web.req.abc;

import lombok.Getter;
import lombok.Setter;
import util.AssertUtils;

import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/22
 * @Purpose: 农业银行支付
 */
@Getter
@Setter
public class ABCPayIM {

    /**
     * 设定订单的属性
     */
    private DicOrder dicOrderIm;

    /**
     * 订单明细
     */
    private List<Orderitem> orderitemIm;

    /**
     * 支付请求对象
     */
    private DicRequest dicRequest;

    /**
     * 二级商户编号    向二级商户入账时必输
     */
    private String SplitMerchantID;

    /**
     * 入账金额    向二级商户入账时必输
     */
    private String SplitAmount;

    public ABCPayIM(DicOrder dicOrderIm, List<Orderitem> orderitemIm, DicRequest dicRequest) {
        this.dicOrderIm = dicOrderIm;
        this.orderitemIm = orderitemIm;
        this.dicRequest = dicRequest;
        SplitMerchantID = "";
        SplitAmount = "";
    }

    public ABCPayIM(DicOrder dicOrderIm, List<Orderitem> orderitemIm, DicRequest dicRequest,String splitMerchantID,String splitAmount) {
        this.dicOrderIm = dicOrderIm;
        this.orderitemIm = orderitemIm;
        this.dicRequest = dicRequest;
        SplitMerchantID = AssertUtils.checkEmptyBack(splitMerchantID);
        SplitAmount = AssertUtils.checkEmptyBack(splitAmount);
    }

}
