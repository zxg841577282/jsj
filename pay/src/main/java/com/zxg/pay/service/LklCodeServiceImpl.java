package com.zxg.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.zxg.pay.config.LklCodeConfig;
import com.zxg.pay.web.enums.code.LKL_PayModeEnum;
import com.zxg.pay.web.enums.code.LKL_TransTypeEnum;
import com.zxg.pay.web.req.lakala.code.QR_ORDER_CREATE_IM;
import com.zxg.pay.web.req.lakala.code.QR_REFUND_ORDER_IM;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import other.ResultException;
import util.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/23
 * @Purpose:
 */
@Service
public class LklCodeServiceImpl implements LklCodeService {

//    public static void main(String[] args) {
//        //统一预下单 微信JSAPI支付
//        QR_ORDER_PRE_ORDERPAY(LKL_PayModeEnum.WECHAT,new BigDecimal(1), "olpr-0m3jzUK-xUGt3_zZo4rjFPI",LKL_TransTypeEnum.JSAPI,"cs19112714330706");
//
//        //动态二维码
////        QR_ORDER_CREATE(0.01,"cs19112714330705");
//
//        //退货交易
////        QR_REFUND_ORDER(new Date(), 0.01, "", "20191119110113100266111200243080");
//
////        QR_REFUND_QUERY_ORDER("8a31dd996e696242016e7dc9dc8e0191","");
//
////        QR_ORDER_QUERY("2AEC2553CFE249AEA795346DB57AE23D","","");
//    }

    /**
     * 统一预下单
     *
     * @param modeEnum 支付模式
     * @param amount   下单金额
     * @param openId   用户的OPENID
     * @param typeEnum 商户类型
     * @param orderId  订单号
     * @return
     */
    public static JSONObject QR_ORDER_PRE_ORDERPAY(LKL_PayModeEnum modeEnum, BigDecimal amount, String openId, LKL_TransTypeEnum typeEnum, String orderId) {
        //第一步：填入参数
        Map<String, Object> map = getMap("QR_ORDER_PRE_ORDERPAY");

        //请求参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("shopNo", "822290070111135");                  //商户号
        paramMap.put("termId", "29034705");                          //终端号
        paramMap.put("payMode", modeEnum.getCode());                //支付模式
        paramMap.put("amount", amountNormalization(amount));         //金额
        paramMap.put("transType", typeEnum.getCode());               //商户类型
        paramMap.put("orderId", orderId);                            //订单号
        paramMap.put("notifyUrl", LklCodeConfig.notifyUrl);         //订单号

        if (typeEnum.equals(LKL_TransTypeEnum.JSAPI)) {              //transType=JSAPI时，有此值时
            paramMap.put("openId", openId);                          //微信支付openId 支付宝支付buyer_id
            paramMap.put("appId", LklCodeConfig.testAPPID);         //appId wx9ef39b708f16694d
        }

        return getSignToMap(map, paramMap);
    }

    /**
     * 动态二维码
     *
     * @return
     */
    @Override
    public JSONObject QR_ORDER_CREATE(QR_ORDER_CREATE_IM im) {
        //第一步：填入参数
        Map<String, Object> map = getMap("QR_ORDER_CREATE");

        //请求参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("shopNo", im.getShopNo());                      //商户号 LklCodeConfig.testUser
        paramMap.put("termId", im.getTermId());                      //终端号 LklCodeConfig.testNumber
        paramMap.put("shopName", im.getShopName());                  //商户名称
        paramMap.put("amount", amountNormalization(im.getAmount())); //金额
        paramMap.put("orderId", im.getOrderId());                    //订单号
        paramMap.put("subject", im.getSubject());                    //订单标题
        paramMap.put("description", im.getDescription());            //订单描述
        paramMap.put("notifyUrl", LklCodeConfig.lklDynamicNotifyUrl);//回调地址
        paramMap.put("validTime", "300");                            //时间
//        paramMap.put("shopType","商户类型");                      //商户类型
//        paramMap.put("location","1");                             //地理位置

        return getSignToMap(map, paramMap);
    }

    /**
     * 退货交易
     *
     * @return
     */
    @Override
    public JSONObject QR_REFUND_ORDER(QR_REFUND_ORDER_IM im) {
        //第一步：填入参数
        Map<String, Object> map = getMap("QR_REFUND_ORDER");

        //请求参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("shopNo", im.getShopNo());                              //商户号
        paramMap.put("termId", im.getTermId());                              //终端号
        paramMap.put("oriDate", DateUtil.getMMDDDate(im.getOriDate()));     //原交易日期
        paramMap.put("amount", amountNormalization(im.getAmount()));         //退款金额
        paramMap.put("tradeNo", im.getTradeNo());                            //平台交易单号
        paramMap.put("lklOrderNo", im.getLklOrderNo());                      //拉卡拉商户订单号
        paramMap.put("srefno", RandomNumber.getStringRandom(32));    //系统参考号
        paramMap.put("refundOrderId", im.getRefundOrderId());               //商户退款单号

        return getSignToMap(map, paramMap);
    }

    /**
     * 退款查询
     *
     * @param refundTradeNo 平台退款单号
     * @param refundOrderId 商户退款单号
     * @return
     */
    @Override
    public JSONObject QR_REFUND_QUERY_ORDER(String refundTradeNo, String refundOrderId) {
        //第一步：填入参数
        Map<String, Object> map = getMap("QR_REFUND_QUERY_ORDER");

        //请求参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("shopNo", LklCodeConfig.testUser);
        paramMap.put("termId", LklCodeConfig.testNumber);
        paramMap.put("refundTradeNo", refundTradeNo);
//        paramMap.put("refundOrderId",refundOrderId);

        return getSignToMap(map, paramMap);
    }

    /**
     * 查询商户订单
     *
     * @return
     */
    public static JSONObject QR_ORDER_QUERY(String tradeNo, String orderId, String lklOrderNo) {
        //第一步：填入参数
        Map<String, Object> map = getMap("QR_ORDER_QUERY");

        //请求参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("shopNo", LklCodeConfig.testUser);
        paramMap.put("termId", LklCodeConfig.testNumber);
        paramMap.put("tradeNo", tradeNo);
        paramMap.put("orderId", orderId);
        paramMap.put("lklOrde rNo", lklOrderNo);
//        paramMap.put("refundOrderId",refundOrderId);

        return getSignToMap(map, paramMap);
    }

    /**
     * 获取公共参数
     *
     * @param cmd
     * @return
     */
    private static Map<String, Object> getMap(String cmd) {
        Map<String, Object> map = new HashMap<>();
        map.put("appid", LklCodeConfig.testAPPID);                  //应用表示
        map.put("token", LklCodeConfig.testTOKEN);                  //授权码
        map.put("timestamp", System.currentTimeMillis() + "");        //时间戳
        map.put("rnd", RandomNumber.getStringRandom(32));   //随机数
        map.put("ver", "1.0.0");                                     //版本号
        map.put("reqId", RandomNumber.getStringRandom(32));  //请求序列号
        map.put("cmd", cmd);                                         //指令，参见指令说明
        map.put("channel", "LAKALA");                                //渠道
        map.put("productType", "LKL_APP_QR");                        //产品类型，由拉卡拉分配，默认填写LKL_APP_QR
        map.put("orderExtInfo", new HashMap<>());                    //无扩展订单内容
        map.put("termExtInfo", new HashMap<>());                     //无终端信息

        return map;
    }

    /**
     * 生成签名并组合参数并请求
     *
     * @param map
     * @param paramMap
     * @return
     */
    private static JSONObject getSignToMap(Map<String, Object> map, Map<String, String> paramMap) {
        map.put("reqData", paramMap);

        //设置签名
        String reqData = getReqData(paramMap, LklCodeConfig.testSecret_Key);
        System.out.println("reqDate = " + reqData);
        String sign = MD5Util.getMD5(reqData);
        System.out.println("sign = " + sign);
        map.put("sign", sign);

        String string = MapJsonStringUtil.MapToJsonString(map);
        System.out.println("请求参数：" + string);

        System.out.println("###############################");
        System.out.println("###############################");
        System.out.println("###############################");
        //POST请求，提交参数

        String back = HttpClientUtil.doLklPayPost(string, LklCodeConfig.testUrl);
        JSONObject backInfo = MapJsonStringUtil.StringToJson(back);

        System.out.println("返回信息：" + backInfo);

        //说明请求失败
        if (!backInfo.get("retCode").equals("000000")) {
            String retMsg = backInfo.getString("retMsg");
            throw new ResultException(retMsg);
        }

        return backInfo;
    }

    /**
     * 拼接请求参数
     */
    private static String getReqData(Map paramMap, String secretKey) {
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(paramMap.entrySet());

        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                // 指定排序器按照降序排列
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        StringBuilder content = new StringBuilder("");
        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, String> stringStringEntry = infoIds.get(i);
            content.append(stringStringEntry.getKey()).append("=").append(stringStringEntry.getValue()).append("|");
        }
        return content.toString() + secretKey;
    }

    /**
     * 金额规范化
     */
    private static String amountNormalization(BigDecimal amount) {
//        第一步：乘以100，以分为单位，1元 = 100
        BigDecimal b = amount.multiply(new BigDecimal(100));
        StringBuilder a = new StringBuilder(b.intValue() + "");

        //第二步：长度不都左补0，总长度为12位
        int length = 12 - a.length();
        for (int i = 0; i < length; i++) {
            a.insert(0, "0");
        }
        //第三步：输出规范后的金额
        return a.toString();
    }
}
