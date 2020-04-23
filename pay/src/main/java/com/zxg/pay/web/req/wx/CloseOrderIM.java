package com.zxg.pay.web.req.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/21
 * @Purpose:
 */
@Getter
@Setter
public class CloseOrderIM extends BaseWxInfoIM{

    //系统订单号
    private String out_trade_no;

    public CloseOrderIM(String appid, String mch_id, String mchidKey, String out_trade_no) {
        super(appid, mch_id, mchidKey);
        this.out_trade_no = out_trade_no;
    }
}
