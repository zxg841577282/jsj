package com.zxg.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.zxg.pay.web.req.lakala.paymax.CreateIM;

public interface PayMaxService {

    /**
     * 发起支付
     */
    String charge(CreateIM im);

    /**
     * 查询充值订单
     */
    JSONObject retrieve(String orderId);
}
