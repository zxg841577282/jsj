package com.zxg.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zxg.pay.web.req.lakala.paymax.Charge;
import com.zxg.pay.web.req.lakala.paymax.CreateIM;
import org.springframework.stereotype.Service;
import other.ResultException;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/23
 * @Purpose:
 */
@Service
public class PayMaxServiceImpl implements PayMaxService {

    /**
     * 创建充值订单
     */
    @Override
    public String charge(CreateIM im) {
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", String.valueOf(im.getAmount().setScale(2, RoundingMode.HALF_DOWN)));
        chargeMap.put("subject", im.getSubject());
        chargeMap.put("body", im.getBody());
        chargeMap.put("order_no", im.getOrderNo());
        chargeMap.put("channel", "lakala_web_fast");
        chargeMap.put("client_ip", im.getClient_ip());
        chargeMap.put("app", im.getAppKey());
        chargeMap.put("currency", "CNY");
        chargeMap.put("description", im.getDescription());
        //请根据渠道要求确定是否需要传递extra字段
        Map<String, Object> extra = new HashMap<String, Object>();
        extra.put("user_id", im.getUserId());
        extra.put("return_url", im.getReturn_url());
        chargeMap.put("extra", extra);
        try {
            Charge charge = Charge.create(chargeMap);

            if (charge.getReqSuccessFlag()) {//http请求成功
                Map<String, Object> credential = charge.getCredential();
                return (String) credential.get("lakala_web_fast");
            } else {//http请求失败
                System.out.println(JSON.toJSONString(charge));
                String failureCode = charge.getFailureCode();
                String failureMsg = charge.getFailureMsg();
                System.out.println("failureCode:" + failureCode);
                System.out.println("failureMsg:" + failureMsg);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultException("拉卡拉PaxMax支付异常");
        }
    }

    /**
     * 查询充值订单
     */
    @Override
    public JSONObject retrieve(String orderId) {
        try {
            Charge charge = Charge.retrieve(orderId);

            if (charge.getReqSuccessFlag()) {//http请求成功
                Map<String, Object> credential = charge.getCredential();
                return (JSONObject) credential.get("alipay_app");
            } else {//http请求失败
                System.out.println(JSON.toJSONString(charge));
                String failureCode = charge.getFailureCode();
                String failureMsg = charge.getFailureMsg();
                System.out.println("failureCode:" + failureCode);
                System.out.println("failureMsg:" + failureMsg);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultException("拉卡拉PaxMax查询充值订单异常");
        }
    }

}
