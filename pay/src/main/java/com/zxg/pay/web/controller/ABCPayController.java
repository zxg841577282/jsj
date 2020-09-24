package com.zxg.pay.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.zxg.pay.service.ABCService;
import com.zxg.pay.web.req.abc.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import other.R;
import util.SnowFlake;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/22
 * @Purpose:
 */
@Api(description = "农业银行支付")
@RestController
@RequestMapping("/abc/pay")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ABCPayController {
    private final ABCService abcService;

    @Value("${wx-appid}")
    private String APPID;

    @ApiOperation(value = "农业之微信支付")
    @PostMapping("/wxPay")
    public JSONObject wxPay(BigDecimal totalFee) {
        String orderNo = SnowFlake.getId();
        String openId = "oHXtL5OeHTv_03G0t4891aR_Sn1I";
        String productName = "测试商品";
        String resultNotifyURL = "http://localhost:8081/MerchantResult.jsp";

        //订单的属性
        DicOrderByWX dicOrderIm = new DicOrderByWX(orderNo, totalFee, APPID, openId);

        //订单明细
        List<Orderitem> orderitemIm = new ArrayList<>();
        Orderitem orderitem = new Orderitem(productName);
        orderitemIm.add(orderitem);

        //支付请求对象
        DicRequestWX dicRequest = new DicRequestWX(resultNotifyURL);

        //构造请求参数
        ABCWXPayIM im = new ABCWXPayIM(dicOrderIm, orderitemIm, dicRequest);

        return abcService.payByWx(im);
    }

    @ApiOperation(value = "农业银行支付")
    @PostMapping("/abcPay")
    public JSONObject abcPay(BigDecimal totalFee) {
        String orderNo = SnowFlake.getId();
        String productName = "测试商品";
        String resultNotifyURL = "http://localhost:8081/MerchantResult.jsp";

        //订单的属性
        DicOrder dicOrder = new DicOrder(orderNo, totalFee);

        //订单明细
        List<Orderitem> orderitemIm = new ArrayList<>();
        Orderitem orderitem = new Orderitem(productName);
        orderitemIm.add(orderitem);

        //支付请求对象
        DicRequest dicRequest = new DicRequest(resultNotifyURL);

        //构造请求参数
        ABCPayIM im = new ABCPayIM(dicOrder, orderitemIm, dicRequest);

        return abcService.payByABC(im);
    }

    @ApiOperation(value = "农业订单单笔查询")
    @PostMapping("/getABCOrder")
    public JSONObject getABCOrder(String orderNo, Boolean type) {
        GetInfoSingle im = new GetInfoSingle("ImmediatePay", orderNo, type ? "1" : "0");

        return abcService.getABCOrder(im);
    }

    @ApiOperation(value = "农业订单单笔退款")
    @PostMapping("/refundSingle")
    public JSONObject refundSingle() {
        String orderDate = "";
        String orderTime = "";
        String orderNo = "";
        String newOrderNo = "";
        String trxAmount = "";

        RefundSingle refundSingle = new RefundSingle(orderDate, orderTime, orderNo, newOrderNo, trxAmount);

        RefundSingleIM im = new RefundSingleIM(refundSingle);

        return abcService.refundSingle(im);
    }

}
