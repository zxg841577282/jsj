package com.zxg.pay.web.req.wx;

import lombok.Getter;
import lombok.Setter;
import util.AssertUtils;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/21
 * @Purpose: 微信查询订单
 */
@Getter
@Setter
public class SelectOrderIM extends BaseWxInfoIM {

    //系统订单号
    private String out_trade_no;

    //微信订单号
    private String transaction_id;

    public SelectOrderIM(String appid, String mch_id, String mchidKey, String out_trade_no, String transaction_id) {
        super(appid, mch_id, mchidKey);
        AssertUtils.isAllNull(out_trade_no, transaction_id, "系统订单号和微信订单号必须填写一个");
        this.out_trade_no = (out_trade_no == null ? "" : out_trade_no);
        this.transaction_id = (transaction_id == null ? "" : transaction_id);
    }
}
