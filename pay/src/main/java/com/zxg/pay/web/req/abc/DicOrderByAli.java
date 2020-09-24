package com.zxg.pay.web.req.abc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import other.ResultException;
import util.DateUtil;

import java.util.Date;

/**
 * @Author: zhou_xg
 * @Date: 2020/3/10 17:25
 * @Purpose:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DicOrderByAli {
    /**
     * ALI_PC：网站支付
     * ALI_WAP：WAP支付
     * ALI_APP：APP支付
     * ALI_PRECREATE：线下主扫支付宝
     * ALI_CREATE：线下静态一码多付
     * ALI_PAY：刷卡支付
     */
    private String PayTypeID;//必须 交易类型
    /**
     * YYYY/MM/DD格式
     */
    private String OrderDate;//必须 订单日期
    /**
     * HH:MM:SS格式
     */
    private String OrderTime;//必须 订单时间

    private String ExpiredDate;//设定订单保存时间

    private String PAYED_RETURN_URL;//支付后回调地址

    private String CurrencyCode;//必须 交易币种
    private String OrderNo;//必须 支付订单编号
    private String OrderAmount;//必须 交易金额

    private String Fee;//手续费金额
    /**
     * 交易类型为“线下静态一码多付”时必输，输入客户的支付宝 userid；
     * 交易类型为"刷卡支付"时必输，输入客户支付宝的授权码 auth_code
     */
    private String AccountNo;//指定付款账户
    private String ReceiverAddress;//收货地址
    /**
     * 1：分期；0：不分期
     */
    private String InstallmentMark;//必须 分期标识
    private String InstallmentCode;//分期代码 分期标识为“1”时必须设定
    private String InstallmentNum;//分期期数 分期标识为“1”时必须设定，0-99
    private String BuyIP;//客户 IP
    private String OrderDesc;//必须 订单说明
    private String orderTimeoutDate;//订单有效期
    private String WapQuitUrl;//WAP 支付中途退出返回网址
    private String PcQrPayMode;//PC 扫码支付方式  PC 网站支付时必输
    private String PcQrCodeWidth;//自定义二维码宽度 PC 扫码支付方式为 4 时必输
    private String TimeoutExpress;//支付宝订单有效期 取值范围 1m-15d,不接收小数点（m-分钟，h-小时，d-天）
    private String LimitPay;//指定不能使用贷记卡时填入no_credit
    private String ChildMerchantNo;//子商户(大商户模式)

    public String getOrderDate() {
        return OrderDate = DateUtil.getDate(new Date());
    }

    public String getOrderTime() {
        return OrderTime = DateUtil.getTime(new Date());
    }

    //设定订单保存时间
    public String getExpiredDate() {
        return ExpiredDate = ExpiredDate == null ? "" : ExpiredDate;
    }

    //当交易币种为空时，默认使用人民币
    public String getCurrencyCode() {
        return CurrencyCode = CurrencyCode == null ? "156" : CurrencyCode;
    }

    public String getOrderNo() {
        return OrderNo = OrderNo == null ? "订单单号" : OrderNo;
    }

    public String getFee() {
        return Fee = Fee == null ? "" : Fee;
    }

    public String getAccountNo() {
        if (PayTypeID.equals("ALI_CREATE") && AccountNo == null) {
            throw new ResultException("交易类型为“线下静态一码多付”时必输，输入客户的支付宝 userid");
        }
        if (PayTypeID.equals("ALI_PAY") && AccountNo == null) {
            throw new ResultException("输入客户支付宝的授权码 auth_code");
        }

        return AccountNo = AccountNo == null ? "" : AccountNo;
    }

    public String getReceiverAddress() {
        return ReceiverAddress = ReceiverAddress == null ? "" : ReceiverAddress;
    }

    public String getInstallmentCode() {
        if (InstallmentMark.equals("1") && InstallmentCode == null) {
            throw new ResultException("当使用分期支付时,分期代码必须设定");
        }
        return InstallmentCode = InstallmentCode == null ? "" : InstallmentCode;
    }

    public String getInstallmentNum() {
        if (InstallmentMark.equals("1") && InstallmentNum == null) {
            throw new ResultException("当使用分期支付时,分期期数必须设定");
        }
        return InstallmentNum = InstallmentNum == null ? "" : InstallmentNum;
    }

    public String getBuyIP() {
        return BuyIP = BuyIP == null ? "" : BuyIP;
    }

    public String getOrderTimeoutDate() {
        return orderTimeoutDate = orderTimeoutDate == null ? "" : orderTimeoutDate;
    }


    public String getPayTypeID() {
        if (PayTypeID == null) {
            throw new ResultException("交易类型类型不能为空");
        }
        return PayTypeID;
    }

    public String getOrderDesc() {
        if (OrderDesc == null) {
            throw new ResultException("订单说明不能为空");
        }
        return OrderDesc;
    }

    public String getInstallmentMark() {
        if (InstallmentMark == null) {
            throw new ResultException("是否分期标识不能为空");
        }
        return InstallmentMark;
    }

    public String getOrderAmount() {
        if (OrderAmount == null) {
            throw new ResultException("交易金额不能为空");
        }
        return OrderAmount;
    }

    //默认不支持借贷卡
    public String getLimitPay() {
        return LimitPay = LimitPay == null ? "no_credit" : LimitPay;
    }

    public String getPAYED_RETURN_URL() {
        return PAYED_RETURN_URL = PAYED_RETURN_URL == null ? "" : PAYED_RETURN_URL;
    }

    public String getWapQuitUrl() {
        return WapQuitUrl = WapQuitUrl == null ? "" : WapQuitUrl;
    }

    public String getPcQrPayMode() {
        if (PayTypeID.equals("ALI_PC") && PcQrPayMode == null) {
            throw new ResultException("PC 网站支付时必输PC 扫码支付方式");
        }
        return PcQrPayMode = PcQrPayMode == null ? "" : PcQrPayMode;
    }

    public String getPcQrCodeWidth() {
        if (PcQrPayMode.equals("4") && PcQrCodeWidth == null) {
            throw new ResultException("PC 扫码支付方式为 4 时必输自定义二维码宽度");
        }
        return PcQrCodeWidth = PcQrCodeWidth == null ? "" : PcQrCodeWidth;
    }

    //默认有效时间15分钟
    public String getTimeoutExpress() {
        return TimeoutExpress = TimeoutExpress == null ? "15m" : TimeoutExpress;
    }

    public String getChildMerchantNo() {
        return ChildMerchantNo = ChildMerchantNo == null ? "" : ChildMerchantNo;
    }
}
