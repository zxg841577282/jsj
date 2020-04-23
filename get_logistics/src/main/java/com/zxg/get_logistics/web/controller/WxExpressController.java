package com.zxg.get_logistics.web.controller;

import com.zxg.get_logistics.other.MultiRequestBody;
import com.zxg.get_logistics.service.wx.WxExpressService;
import com.zxg.get_logistics.web.req.wx.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/19 0019
 * @Purpose:
 */
@Api(description = "微信-物流模板")
@RestController
@RequestMapping("/wx/expressTemplate")
public class WxExpressController {
    @Autowired
    WxExpressService wxExpressService;

    @ApiOperation(value = "生成运单")
    @PostMapping("/addOrder")
    public String addOrder(@MultiRequestBody WXAddOrderIM wxAddOrderIM,
                      @MultiRequestBody SenderOrReceiverIM senderIM,
                      @MultiRequestBody SenderOrReceiverIM receiverIM,
                      @MultiRequestBody ShopIM shopIM,
                      @MultiRequestBody CargoIM cargoIM,
                      @MultiRequestBody InsuredIM insuredIM,
                      @MultiRequestBody ServiceIM serviceIM){
        return wxExpressService.addOrder(wxAddOrderIM,senderIM,receiverIM,shopIM,cargoIM,insuredIM,serviceIM);
    }

    @ApiOperation(value = "取消运单")
    @PostMapping("/cancelOrder")
    public String cancelOrder(@RequestBody CancelOrderIM cancelOrderIM){
        return wxExpressService.cancelOrder(cancelOrderIM);
    }

    @ApiOperation(value = "获取支持的快递公司列表")
    @GetMapping("/getAllDelivery")
    public String getAllDelivery(String access_token){
        return wxExpressService.getAllDelivery(access_token);
    }

    @ApiOperation(value = "获取运单数据")
    @PostMapping("/getOrder")
    public String getOrder(@RequestBody GetOrderIM getOrderIM){
        return wxExpressService.getOrder(getOrderIM);
    }

    @ApiOperation(value = "查询运单轨迹")
    @PostMapping("/getPath")
    public String getPath(@RequestBody GetPathIM getPathIM){
        return wxExpressService.getPath(getPathIM);
    }

    @ApiOperation(value = "获取电子面单余额")
    @GetMapping("/getQuota")
    public String getQuota(String access_token,String biz_id){
        return wxExpressService.getQuota(access_token,biz_id);
    }

}
