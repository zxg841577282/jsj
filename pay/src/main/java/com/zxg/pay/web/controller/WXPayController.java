package com.zxg.pay.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.zxg.pay.service.WxPayService;
import com.zxg.pay.web.req.wx.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import util.SnowFlake;

import java.math.BigDecimal;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/21
 * @Purpose: 微信普通商户支付
 */
@Api(description = "微信普通商户支付")
@RestController
@RequestMapping("/wx/pay")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WXPayController {
    private final WxPayService wxJsApiPayService;

    @Value("${wx-appid}")
    private String APPID;

    @Value("${wx-mchId}")
    private String MCHID;

    @Value("${wx-mchKey}")
    private String MCH_KEY;

    @ApiOperation(value = "JSAPI支付")
    @PostMapping("/jsApiPay")
    public JSONObject jsApiPay(BigDecimal totalFee) {
        String orderNo = SnowFlake.getId();
        String openId = "oHXtL5OeHTv_03G0t4891aR_Sn1I";
        String ip = "47.114.2.38";

        JsApiPayIM im = new JsApiPayIM(APPID, MCHID, MCH_KEY,
                orderNo, openId, totalFee, null, ip, null, null);
        return wxJsApiPayService.payByJsApi(im);
    }

    @ApiOperation(value = "Native支付")
    @PostMapping("/nativePay")
    public JSONObject nativePay(BigDecimal totalFee) {
        String orderNo = SnowFlake.getId();
        String openId = "oHXtL5OeHTv_03G0t4891aR_Sn1I";
        String ip = "47.114.2.38";

        JsApiPayIM im = new JsApiPayIM(APPID, MCHID, MCH_KEY,
                orderNo, openId, totalFee, null, ip, null, null);
        return wxJsApiPayService.payByNative(im);
    }

    @ApiOperation(value = "查询订单")
    @GetMapping("/findOrder")
    public JSONObject findOrder(String out_trade_no, String transaction_id) {
        SelectOrderIM im = new SelectOrderIM(APPID, MCHID, MCH_KEY, out_trade_no, transaction_id);
        return wxJsApiPayService.select(im);
    }

    @ApiOperation(value = "关闭订单")
    @PutMapping("/closeOrder")
    public JSONObject closeOrder(String out_trade_no) {
        CloseOrderIM im = new CloseOrderIM(APPID, MCHID, MCH_KEY, out_trade_no);
        return wxJsApiPayService.close(im);
    }

    @ApiOperation(value = "申请退款")
    @PostMapping("/refund")
    public JSONObject refund(String out_trade_no, BigDecimal totalFee, BigDecimal refundFee) {
        String out_refund_no = SnowFlake.getId();

        RefundIM im = new RefundIM(APPID, MCHID, MCH_KEY,
                out_trade_no, out_refund_no, totalFee, refundFee, null, null);
        return wxJsApiPayService.refund(im);
    }

    @ApiOperation(value = "查询退款")
    @PostMapping("/selectRefund")
    public JSONObject selectRefund(String transaction_id, String out_trade_no, String out_refund_no, String refund_id) {
        SelectRefundIM im = new SelectRefundIM(APPID, MCHID, MCH_KEY, transaction_id, out_trade_no, out_refund_no, refund_id);
        return wxJsApiPayService.select_Refund(im);
    }

}
