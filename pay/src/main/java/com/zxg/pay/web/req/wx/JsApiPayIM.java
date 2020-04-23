package com.zxg.pay.web.req.wx;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/21
 * @Purpose: 微信JSAPI支付
 */
@Getter
@Setter
public class JsApiPayIM extends BaseWxInfoIM{
    //订单号
    @NotNull(message = "订单号不能为NULL")
    private String out_trade_no;

    //支付用户的OPENID
    @NotNull(message = "支付用户的OPENID不能为NULL")
    private String openId;

    //订单支付金额
    @NotNull(message = "订单支付金额不能为NULL，单位：元，保留2位小数")
    private BigDecimal totalFee;

    //商品信息
    private String body;

    //本机IP
    private String spbill_create_ip;

    //回调地址
    @NotNull(message = "回调地址不能为NULL")
    private String notify_url;

    //有效时间，默认30分钟
    private String time_expire;

    //默认回调地址
    private static String defaultNotifyUrl = "http://jielong.siginfo.cn:8888/jielong/pay/asyncNoticeWxPay";


    public JsApiPayIM(String appid, String mch_id, String mchidKey, String out_trade_no, String openId, BigDecimal totalFee, String body, String ip, String notify_url, String time_expire) {
        super(appid,mch_id,mchidKey);
        this.out_trade_no = out_trade_no;
        this.openId = openId;
        this.totalFee = totalFee;
        this.body = (StringUtils.isEmpty(body) ?"支付内容":body);
        this.spbill_create_ip = (ip==null?"":ip);

        //回调地址为空则使用默认回调地址
        this.notify_url = (StringUtils.isEmpty(notify_url)?defaultNotifyUrl:notify_url);

        //过期时间为空则默认30分钟
        if (time_expire==null){
            SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmss");
            this.time_expire = ft.format(new Date().getTime()+30*1000*60);
        }else {
            this.time_expire = time_expire;
        }
    }
}
