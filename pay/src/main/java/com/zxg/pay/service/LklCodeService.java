package com.zxg.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.zxg.pay.web.req.lakala.code.QR_ORDER_CREATE_IM;
import com.zxg.pay.web.req.lakala.code.QR_REFUND_ORDER_IM;

public interface LklCodeService {

    JSONObject QR_ORDER_CREATE(QR_ORDER_CREATE_IM im);

    JSONObject QR_REFUND_ORDER(QR_REFUND_ORDER_IM im);

    JSONObject QR_REFUND_QUERY_ORDER(String refundTradeNo, String refundOrderId);
}
