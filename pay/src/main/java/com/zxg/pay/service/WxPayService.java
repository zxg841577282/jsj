package com.zxg.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.zxg.pay.web.req.wx.*;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/21
 * @Purpose:
 */

public interface WxPayService {
    /**
     * JSAPI支付
     */
    JSONObject payByJsApi(JsApiPayIM im);

    /**
     * Native支付
     */
    JSONObject payByNative(JsApiPayIM im);

    /**
     * 查询订单
     */
    JSONObject select(SelectOrderIM im);

    /**
     * 关闭订单
     */
    JSONObject close(CloseOrderIM im);

    /**
     * 退款申请
     */
    JSONObject refund(RefundIM im);

    /**
     * 查询退款进度
     */
    JSONObject select_Refund(SelectRefundIM im);
}
