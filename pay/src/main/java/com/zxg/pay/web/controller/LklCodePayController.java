package com.zxg.pay.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.zxg.pay.service.LklCodeService;
import com.zxg.pay.web.req.lakala.code.QR_ORDER_CREATE_IM;
import com.zxg.pay.web.req.lakala.code.QR_REFUND_ORDER_IM;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import util.SnowFlake;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/23
 * @Purpose:
 */
@Api(description = "拉卡拉扫码支付")
@RestController
@RequestMapping("/lkl/code")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LklCodePayController {

    private final LklCodeService lklCodeService;


    @Value("${lkl-code-shopNo}")
    private String shopNo;

    @Value("${lkl-code-termId}")
    private String termId;


    @ApiOperation(value = "动态二维码")
    @PostMapping("/pay")
    public JSONObject pay(BigDecimal totalFee) {
        String orderNo = SnowFlake.getId();
        String shopName = "商户名称";

        QR_ORDER_CREATE_IM im = new QR_ORDER_CREATE_IM(shopNo,termId,shopName,totalFee,orderNo);

        return lklCodeService.QR_ORDER_CREATE(im);
    }

    @ApiOperation(value = "退款")
    @PutMapping("/refund")
    public JSONObject refund(BigDecimal totalFee,String tradeNo,String lklOrderNo) {
        Date oriDate = new Date();
        String refundOrderId = SnowFlake.getId();

        QR_REFUND_ORDER_IM im = new QR_REFUND_ORDER_IM(shopNo,termId,oriDate,totalFee,tradeNo,lklOrderNo,refundOrderId);

        return lklCodeService.QR_REFUND_ORDER(im);
    }

    @ApiOperation(value = "查询退款")
    @GetMapping("/getRefund")
    public JSONObject getRefund(String refundTradeNo, String refundOrderId) {

        return lklCodeService.QR_REFUND_QUERY_ORDER(refundTradeNo,refundOrderId);
    }


}
