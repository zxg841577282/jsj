package com.zxg.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.zxg.pay.util.PayGeneralMethod;
import com.zxg.pay.util.XmlJsonMapUtil;
import com.zxg.pay.web.req.wx.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import other.ResultException;
import util.AssertUtils;
import util.HttpClientUtil;
import util.ValidationUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/21
 * @Purpose:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WxPayServiceImpl implements WxPayService {

    //统一支付
    private static String Pay_Url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //查询订单
    private static String Select_Url = "https://api.mch.weixin.qq.com/pay/orderquery";
    //关闭订单
    private static String Close_Url = "https://api.mch.weixin.qq.com/pay/closeorder";
    //申请退款
    private static String Refund_Url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    //查询退款
    private static String Select_Refund_Url = "https://api.mch.weixin.qq.com/pay/refundquery";

    //企业支付到零钱
    private static String WX_Pay_To_Cash_Url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    //企业支付到银行卡
    private static String WX_Pay_To_Bank_Url = "https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank";

    //支付证书地址
    @Value("${wx-cret-addr}")
    private String cretAddr;

    //公钥地址
    @Value("${wx-pem-addr}")
    private String pemAddr;


    /**
     * JSAPI支付
     * @return
     */
    public JSONObject payByJsApi(JsApiPayIM im){
        return pay(im,true);
    }

    /**
     * Native支付
     * @return
     */
    public JSONObject payByNative(JsApiPayIM im){
        return pay(im,false);
    }

    private JSONObject pay(JsApiPayIM im,boolean b){
        //参数校验
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(im);
        AssertUtils.check(!validResult.hasErrors(),validResult.getErrors());

        Map<String, String> map = new HashMap<>();

        map.put("appid", im.getAppid());
        map.put("mch_id", im.getMch_id());
        map.put("nonce_str", PayGeneralMethod.getStringRandom());
        map.put("sign_type", "MD5");
        map.put("body", im.getBody());
        map.put("out_trade_no", im.getOut_trade_no());
        map.put("total_fee", PayGeneralMethod.totalFeeWx(im.getTotalFee()));
        map.put("spbill_create_ip", im.getSpbill_create_ip());
        map.put("notify_url", im.getNotify_url());
        map.put("trade_type", b?"JSAPI":"NATIVE");
        map.put("time_expire", im.getTime_expire());
        map.put("openid", im.getOpenId());

        String paramSign = PayGeneralMethod.getParamSign(map, im.getMchidKey());
        //设置参数签名
        map.put("sign", paramSign);
        //访问链接
        String xmlFromMap = XmlJsonMapUtil.getXMLFromMap(map);
        System.out.println("微信支付请求参数：[" + xmlFromMap + "]");

        String s = HttpClientUtil.doWxPayPost(xmlFromMap, Pay_Url);

        //xml格式转json
        JSONObject jsonObject = XmlJsonMapUtil.XmlToJSONObject(s);
        System.out.println("微信支付返回参数：[" + jsonObject + "]");

        return jsonObject;
    }

    /**
     * 查询订单信息
     * @return
     */
    public JSONObject select(SelectOrderIM im){
        //参数校验
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(im);
        AssertUtils.check(!validResult.hasErrors(),validResult.getErrors());

        Map<String, String> map = new HashMap<>();
        map.put("appid", im.getAppid());
        map.put("mch_id", im.getMch_id());
        map.put("transaction_id", im.getTransaction_id());
        map.put("out_trade_no", im.getOut_trade_no());
        map.put("nonce_str", PayGeneralMethod.getStringRandom());
        //设置参数签名
        map.put("sign", PayGeneralMethod.getParamSign(map, im.getMchidKey()));

        String xmlFromMap = XmlJsonMapUtil.getXMLFromMap(map);
        System.out.println("微信查询订单请求参数：[" + xmlFromMap + "]");

        String s = HttpClientUtil.doWxPayPost(xmlFromMap, Select_Url);

        //xml格式转json
        JSONObject jsonObject = XmlJsonMapUtil.XmlToJSONObject(s);
        System.out.println("微信查询订单返回参数：[" + jsonObject + "]");

        return jsonObject;
    }

    /**
     * 关闭订单
     * @return
     */
    public JSONObject close(CloseOrderIM im){
        //参数校验
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(im);
        AssertUtils.check(!validResult.hasErrors(),validResult.getErrors());

        Map<String, String> map = new HashMap<>();
        map.put("appid", im.getAppid());
        map.put("mch_id", im.getMch_id());
        map.put("out_trade_no", im.getOut_trade_no());
        map.put("nonce_str", PayGeneralMethod.getStringRandom());
        map.put("sign_type", "MD5");
        //设置参数签名
        map.put("sign", PayGeneralMethod.getParamSign(map, im.getMchidKey()));

        String xmlFromMap = XmlJsonMapUtil.getXMLFromMap(map);
        System.out.println("微信关闭订单请求参数：[" + xmlFromMap + "]");

        String s = HttpClientUtil.doWxPayPost(xmlFromMap, Close_Url);

        //xml格式转json
        JSONObject jsonObject = XmlJsonMapUtil.XmlToJSONObject(s);
        System.out.println("微信关闭订单返回参数：[" + jsonObject + "]");

        return jsonObject;
    }

    /**
     * 退款
     * @return
     */
    public JSONObject refund(RefundIM im){
        //参数校验
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(im);
        AssertUtils.check(!validResult.hasErrors(),validResult.getErrors());

        Map<String, String> map = new HashMap<>();
        map.put("appid", im.getAppid());
        map.put("mch_id", im.getMch_id());
        map.put("nonce_str", PayGeneralMethod.getStringRandom());
        map.put("sign_type", "MD5");
        map.put("out_trade_no", im.getOut_trade_no());
        map.put("out_refund_no", im.getOut_refund_no());
        map.put("refund_fee_type", "CNY");
        map.put("total_fee", PayGeneralMethod.totalFeeWx(im.getTotalFee()));
        map.put("refund_fee", PayGeneralMethod.totalFeeWx(im.getRefundFee()));
        map.put("refund_desc", im.getRefund_desc());
        map.put("refund_account", "");
        map.put("notify_url", im.getNotify_url());
        //设置参数签名
        map.put("sign", PayGeneralMethod.getParamSign(map, im.getMchidKey()));

        try {
            String xmlFromMap = XmlJsonMapUtil.getXMLFromMap(map);
            System.out.println("微信退款订单请求参数：[" + xmlFromMap + "]");

            //访问链接
            String s = HttpClientUtil.doWxPayPostCret( xmlFromMap,Refund_Url, cretAddr, im.getMch_id());

            JSONObject jsonObject = XmlJsonMapUtil.XmlToJSONObject(s);
            System.out.println("微信退款订单返回参数：[" + jsonObject + "]");

            return jsonObject;
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultException("申请退款未知异常");
        }
    }

    /**
     * 查询退款进度
     * @return
     */
    public JSONObject select_Refund(SelectRefundIM im){
        //参数校验
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(im);
        AssertUtils.check(!validResult.hasErrors(),validResult.getErrors());

        Map<String, String> map = new HashMap<>();
        map.put("appid", im.getAppid());
        map.put("mch_id", im.getMch_id());
        map.put("nonce_str", PayGeneralMethod.getStringRandom());
        map.put("sign_type", "MD5");
        map.put("transaction_id", im.getTransaction_id());
        map.put("out_trade_no", im.getOut_trade_no());
        map.put("out_refund_no", im.getOut_refund_no());
        map.put("refund_id", im.getRefund_id());
        map.put("offset", "");

        map.put("sign", PayGeneralMethod.getParamSign(map, im.getMchidKey()));

        String xmlFromMap = XmlJsonMapUtil.getXMLFromMap(map);
        System.out.println("微信查询退款进度请求参数：[" + xmlFromMap + "]");

        String s = HttpClientUtil.doWxPayPost(xmlFromMap, Select_Refund_Url);

        JSONObject jsonObject = XmlJsonMapUtil.XmlToJSONObject(s);
        System.out.println("微信查询退款进度返回参数：[" + jsonObject + "]");

        return jsonObject;
    }

    /**
     * 企业支付到零钱
     */
    public JSONObject payToCash(PayToCashIM im){
        Map<String, String> map = new HashMap<>();

        map.put("mchid", im.getMch_id());
        map.put("mch_appid", im.getAppid());
        map.put("nonce_str", PayGeneralMethod.getStringRandom());
        map.put("partner_trade_no", im.getPartner_trade_no());
        map.put("openid", im.getOpenid());
        map.put("check_name", "NO_CHECK");
        map.put("amount", PayGeneralMethod.totalFeeWx(im.getAmount()));
        map.put("desc", im.getDesc());
        map.put("spbill_create_ip", im.getSpbill_create_ip());

        //设置参数签名
        map.put("sign", PayGeneralMethod.getParamSign(map, im.getMchidKey()));

        //参数转换成xml
        String xmlFromMap = XmlJsonMapUtil.getXMLFromMap(map);
        System.out.println("微信企业支付到零钱请求参数：[" + xmlFromMap + "]");

        String s = HttpClientUtil.doWxPayPostCret(xmlFromMap, WX_Pay_To_Cash_Url, cretAddr, im.getMch_id());

        JSONObject jsonObject = XmlJsonMapUtil.XmlToJSONObject(s);
        System.out.println("微信企业支付到零钱返回参数：[" + jsonObject + "]");

        return jsonObject;
    }

    /**
     * 企业支付到银行卡
     */
    public JSONObject payToBank(PayToBankIM im){
        Map<String, String> map = new HashMap<>();

        map.put("mch_id", im.getMch_id());
        map.put("partner_trade_no", im.getOrderNo());
        map.put("nonce_str", PayGeneralMethod.getStringRandom());
        try {
            String bankNo2 = PayGeneralMethod.getDataWithPem(im.getBankNo(),pemAddr);
            map.put("enc_bank_no", bankNo2);
            String bankUser2 = PayGeneralMethod.getDataWithPem(im.getBankUser(),pemAddr);
            map.put("enc_true_name", bankUser2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultException("企业支付至银行卡，加密失败");
        }
        map.put("bank_code", im.getBankCode());
        map.put("amount", PayGeneralMethod.totalFeeWx(im.getTotalFee()));
        map.put("desc", im.getDesc());

        //设置参数签名
        map.put("sign", PayGeneralMethod.getParamSign(map, im.getMchidKey()));

        //参数转换成xml
        String xmlFromMap = XmlJsonMapUtil.getXMLFromMap(map);
        System.out.println("微信企业支付到银行卡请求参数：[" + xmlFromMap + "]");

        String s = HttpClientUtil.doWxPayPostCret(xmlFromMap, WX_Pay_To_Bank_Url, cretAddr, im.getMch_id());

        JSONObject jsonObject = XmlJsonMapUtil.XmlToJSONObject(s);
        System.out.println("微信企业支付到银行卡返回参数：[" + jsonObject + "]");

        return jsonObject;
    }

}
