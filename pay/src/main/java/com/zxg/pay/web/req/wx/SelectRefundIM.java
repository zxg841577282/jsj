package com.zxg.pay.web.req.wx;

import lombok.Getter;
import lombok.Setter;
import util.AssertUtils;

import java.util.Arrays;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/21
 * @Purpose:
 */
@Setter
@Getter
public class SelectRefundIM extends BaseWxInfoIM {
    //原微信单号
    private String transaction_id;
    //原系统单号
    private String out_trade_no;
    //系统退款单号
    private String out_refund_no;
    //微信退款单号
    private String refund_id;

    public SelectRefundIM(String appid, String mch_id, String mchidKey, String transaction_id, String out_trade_no, String out_refund_no, String refund_id) {
        super(appid, mch_id, mchidKey);
        AssertUtils.isListAllNull(Arrays.asList(transaction_id, out_trade_no, out_refund_no, refund_id), "四个单号必须有一个不为null");
        this.transaction_id = (transaction_id == null ? "" : transaction_id);
        this.out_trade_no = (out_trade_no == null ? "" : out_trade_no);
        this.out_refund_no = (out_refund_no == null ? "" : out_refund_no);
        this.refund_id = (refund_id == null ? "" : refund_id);
    }
}
