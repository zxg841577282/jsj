package com.zxg.pay.web.req.abc;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/22
 * @Purpose: 农业订单单笔退款
 */
@Getter
@Setter
public class RefundSingleIM {

    private RefundSingle refundSingle;

    private String SplitMerchantID;

    private String SplitAmount;

    public RefundSingleIM(RefundSingle refundSingle) {
        this.refundSingle = refundSingle;
        SplitMerchantID = "";
        SplitAmount = "";
    }

    public RefundSingleIM(RefundSingle refundSingle, String splitMerchantID, String splitAmount) {
        this.refundSingle = refundSingle;
        SplitMerchantID = splitMerchantID;
        SplitAmount = splitAmount;
    }
}
