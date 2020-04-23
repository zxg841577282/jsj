package com.zxg.pay.web.req.abc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import other.ResultException;

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
public class DicOrder {
    /**
     * ImmediatePay：直接支付
     * PreAuthPay：预授权支付
     * DividedPay：分期支付
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
    private String AccountNo;//支付账户
    private String OrderURL;//订单说明
    private String ReceiverAddress;//收货地址
    /**
     * 1：分期；0：不分期
     */
    private String InstallmentMark;//必须 分期标识
    private String InstallmentCode;//分期代码 分期标识为“1”时必须设定
    private String InstallmentNum;//分期期数 分期标识为“1”时必须设定，0-99
    /**
     * 充值类 0101:支付账户充值
     * 消费类 0201:虚拟类,0202:传统类,0203:实名类
     * 转账类 0301:本行转账,0302:他行转账
     * 缴费类 0401:水费,0402:电费,0403:煤气费,0404:有线电视费,0405:通讯费,
     * 0406:物业费,0407:保险费,0408:行政费用,0409:税费,0410:学费,0499:其他
     * 理财类 0501:基金,0502:理财产品,0599:其他
     */
    private String CommodityType;//必须 商品种类
    private String BuyIP;//客户 IP
    private String OrderDesc;//订单说明
    private String OrderTimeoutDate;//订单有效期

    /**
     * 最基础构造方法 直接支付 不分期 虚拟类商品
     * @param orderNo 订单号
     * @param orderAmount 支付金额
     */
    public DicOrder(String orderNo,BigDecimal orderAmount) {
        PayTypeID = "ImmediatePay";
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
        AccountNo = "";
        OrderURL = "";
        ReceiverAddress = "";
        InstallmentMark = "0";
        InstallmentCode = "";
        InstallmentNum = "";
        CommodityType = "0201";
        BuyIP = "";
        OrderDesc = "订单描述";
        OrderTimeoutDate = "";
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
