package com.zxg.pay.web.req.abc;

import lombok.Data;
import lombok.NoArgsConstructor;
import other.ResultException;
import util.AssertUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Author: zhou_xg
 * @Date: 2020/3/10 17:25
 * @Purpose:
 */
@Data
@NoArgsConstructor
public class DicOrderByWX {
    /**
     * JSAPI：公众号（小程序）支付
     * APP：app支付
     * MICROPAY：刷卡支付
     */
    private String PayTypeID;//必须 交易类型
    /**
     * YYYY/MM/DD格式
     */
    @NotNull(message = "订单日期不能为空")
    private String OrderDate;//必须 订单日期
    /**
     * HH:MM:SS格式
     */
    @NotNull(message = "订单时间不能为空")
    private String OrderTime;//必须 订单时间

    private String ExpiredDate;//设定订单保存时间

    private String CurrencyCode;//必须 交易币种

    @NotNull(message = "支付订单编号不能为空")
    private String OrderNo;//必须 支付订单编号

    @NotNull(message = "交易金额不能为空，元，保留2位小数")
    private String OrderAmount;//必须 交易金额

    private String Fee;//手续费金额
    /**
     * 交易类型为“公众号（小程序）支付”时必输，上送子商户的公众号 APPID，对应微信统一下单接口中的 sub_appid;
     * 交易类型为"app 支付"时必输，上送子商户的应用 APPID，对应微信统一下单接口中的ub_appid；
     * 交易类型为“刷卡支付”时必输，上送授权码 auth_code。
     */
    @NotNull(message = "指定付款账户不能为空")
    private String AccountNo;//指定付款账户

    @NotNull(message = "OpenID不能为空")
    private String OpenID;//交易类型为"公众号（小程序）支付"时必输，对应微信统一下单接口中的 sub_openid
    private String ReceiverAddress;//收货地址
    /**
     * 1：分期；0：不分期
     */
    private String InstallmentMark;//必须 分期标识
    private String InstallmentCode;//分期代码 分期标识为“1”时必须设定
    private String InstallmentNum;//分期期数 分期标识为“1”时必须设定，0-99
    private String BuyIP;//客户 IP

    @NotNull(message = "订单说明不能为空")
    private String OrderDesc;//必须 订单说明
    private String OrderTimeoutDate;//订单有效期
    private String LimitPay;//指定不能使用贷记卡时填入no_credit


    /**
     * 最基础构造方法
     * @param orderNo 订单号
     * @param orderAmount 交易金额
     * @param accountNo appid
     * @param openID 用户openId
     */
    public DicOrderByWX(String orderNo, BigDecimal orderAmount, String accountNo, String openID) {
        PayTypeID = "JSAPI";

        //订单日期
        SimpleDateFormat ODSDF = new SimpleDateFormat("yyyy/MM/dd");
        OrderDate =  ODSDF.format(new Date());

        //订单时间
        SimpleDateFormat OTSDF = new SimpleDateFormat("HH:mm:ss");
        OrderTime = OTSDF.format(new Date());
        ExpiredDate = "";
        CurrencyCode = "156";
        OrderNo = orderNo;
        OrderAmount = (String.valueOf(orderAmount.setScale(2, RoundingMode.HALF_DOWN)));
        Fee = "";
        AccountNo = accountNo;
        OpenID = openID;
        ReceiverAddress = "";
        InstallmentMark = "0";
        InstallmentCode = "";
        InstallmentNum = "";
        BuyIP = "";
        OrderDesc = "订单描述";
        OrderTimeoutDate = "";
        LimitPay = "no_credit";
    }

    /**
     * 默认不支持借贷卡
     * 默认人民币
     * 默认不分期
     * */
    public DicOrderByWX(String payTypeID, String expiredDate, String orderNo, String orderAmount, String fee, String accountNo, String openID, String receiverAddress, String installmentCode, String installmentNum, String buyIP,  String orderDesc, String orderTimeoutDate, String limitPay) {
        PayTypeID = payTypeID;

        //订单日期
        SimpleDateFormat ODSDF = new SimpleDateFormat("yyyy/MM/dd");
        OrderDate =  ODSDF.format(new Date());

        //订单时间
        SimpleDateFormat OTSDF = new SimpleDateFormat("HH:mm:ss");
        OrderTime = OTSDF.format(new Date());
        ExpiredDate = (AssertUtils.checkEmptyBack(expiredDate));
        CurrencyCode = "156";
        OrderNo = orderNo;
        OrderAmount = orderAmount;
        Fee = (AssertUtils.checkEmptyBack(fee));
        AccountNo = accountNo;
        OpenID = openID;
        ReceiverAddress = (AssertUtils.checkEmptyBack(receiverAddress));
        InstallmentMark = "0";
        InstallmentCode = (AssertUtils.checkEmptyBack(installmentCode));
        InstallmentNum = (AssertUtils.checkEmptyBack(installmentNum));
        BuyIP = (AssertUtils.checkEmptyBack(buyIP));
        OrderDesc = (AssertUtils.checkEmptyBack(orderDesc,"订单描述"));
        OrderTimeoutDate = (AssertUtils.checkEmptyBack(orderTimeoutDate));
        LimitPay = (AssertUtils.checkEmptyBack(limitPay,"no_credit"));
    }

    public String getInstallmentCode() {
        if (InstallmentMark.equals("1") && InstallmentCode==null){
            throw new ResultException("当使用分期支付时,分期代码必须设定"); }
        return InstallmentCode = InstallmentCode==null?"":InstallmentCode;
    }

    public String getInstallmentNum() {
        if (InstallmentMark.equals("1") && InstallmentNum==null){
            throw new ResultException("当使用分期支付时,分期期数必须设定"); }
        return InstallmentNum = InstallmentNum==null?"":InstallmentNum;
    }
}
