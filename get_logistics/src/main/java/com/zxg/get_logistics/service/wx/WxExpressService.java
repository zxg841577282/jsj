package com.zxg.get_logistics.service.wx;

import com.alibaba.fastjson.JSONObject;
import com.zxg.get_logistics.web.req.wx.*;


/**
 * @Author: zhou_xg
 * @Date: 2019/8/19 0019
 * @Purpose:
 */

public interface WxExpressService {
    //生成运单
    String addOrder(WXAddOrderIM wxAddOrderIM, SenderOrReceiverIM senderIM, SenderOrReceiverIM receiverIM, ShopIM shopIM, CargoIM cargoIM, InsuredIM insuredIM, ServiceIM serviceIM);
    //取消运单
    String cancelOrder(CancelOrderIM cancelOrderIM);
    //获取支持的快递公司列表
    String getAllDelivery(String access_token);
    //获取运单数据
    String getOrder(GetOrderIM getOrderIM);
    //查询运单轨迹
    String getPath(GetPathIM getPathIM);
    //获取电子面单余额
    String getQuota(String access_token, String biz_id);
}
