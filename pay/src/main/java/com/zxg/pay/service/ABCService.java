package com.zxg.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.zxg.pay.web.req.abc.ABCPayIM;
import com.zxg.pay.web.req.abc.ABCWXPayIM;
import com.zxg.pay.web.req.abc.GetInfoSingle;
import com.zxg.pay.web.req.abc.RefundSingleIM;

public interface ABCService {

    /**
     * 农行支付
     */
    JSONObject payByABC(ABCPayIM im);

    /**
     * 农行微信支付
     */
    JSONObject payByWx(ABCWXPayIM im);

    /**
     * 农业订单单笔查询
     */
    JSONObject getABCOrder(GetInfoSingle getInfoSingle);

    /**
     * 农业订单单笔退款
     */
    JSONObject refundSingle(RefundSingleIM im);
}
