package com.zxg.pay.web.controller;


import com.alibaba.fastjson.JSONObject;
import com.zxg.pay.service.PayMaxService;
import com.zxg.pay.web.req.lakala.paymax.CreateIM;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import util.SnowFlake;

import java.math.BigDecimal;

/**
 * @Author: zhou_xg
 * @Date: 2019/11/19 17:09
 * @Purpose: 拉卡拉PayMax支付
 */
@Api(description = "拉卡拉PayMax支付")
@RestController
@RequestMapping("/lkl/paymax")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LKLPayMaxController {
    private final PayMaxService payMaxService;


    @ApiOperation(value = "发起支付,需自行将from表单中的数据调起页面支付")
    @PostMapping("/pay")
    public String pay(BigDecimal totalFee) {
        String orderNo = SnowFlake.getId();
        String openId = "111111";
        String ip = "47.114.2.38";

        CreateIM im = new CreateIM(totalFee,orderNo,openId,ip);
        return payMaxService.charge(im);
    }

    @ApiOperation(value = "查询充值订单")
    @PostMapping("/selectOrder")
    public JSONObject pay(String orderNo) {

        orderNo = "ch_6f97ac4f57bf182cfe140f34";

        return payMaxService.retrieve(orderNo);
    }

}
