package com.zxg.pay.web.req.abc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import other.ResultException;
import util.AssertUtils;

import javax.validation.constraints.NotNull;

/**
 * @Author: zhou_xg
 * @Date: 2020/3/10 17:33
 * @Purpose:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DicRequestWX {
    /**
     * 充值类 0101:支付账户充值
     * 消费类 0201:虚拟类,0202:传统类,0203:实名类
     * 转账类 0301:本行转账,0302:他行转账
     * 缴费类 0401:水费,0402:电费,0403:煤气费,0404:有线电视费,0405:通讯费,
     * 0406:物业费,0407:保险费,0408:行政费用,0409:税费,0410:学费,0499:其他
     * 理财类 0501:基金,0502:理财产品,0599:其他
     */
    private String CommodityType;//必须 商品种类

    private String PaymentType;//必需 8:微信支付
    /**
     * 1：internet 网络接入
     * 2：手机网络接入
     * 3：数字电视网络接入
     * 4：智能客户端
     */
    private String PaymentLinkType;//必需 交易渠道
    private String NotifyType;//必需 通知方式 1：服务器通知
    @NotNull(message = "回调地址不能位空")
    private String ResultNotifyURL;//必需 通知 URL 地址  0：URL 页面通知  1：服务器通知
    private String MerchantRemarks;//附言
    private String IsBreakAccount;//必需 交易是否分账；是否支持向二级商户入账 0:否 1:是（资金分账商户必须填 1）

    /**
     * 默认虚拟类商品网上交易
     *
     * @param resultNotifyURL 回调通知地址
     */
    public DicRequestWX(String resultNotifyURL) {
        CommodityType = "0201";
        PaymentType = "8";
        PaymentLinkType = "1";
        NotifyType = "1";
        ResultNotifyURL = resultNotifyURL;
        MerchantRemarks = "";
        IsBreakAccount = "0";
    }

    /**
     * 自定义类型商品，网上交易
     *
     * @param commodityType   商品种类
     * @param resultNotifyURL 回调通知地址
     * @param merchantRemarks 附言
     */
    public DicRequestWX(String commodityType, String resultNotifyURL, String merchantRemarks) {
        CommodityType = commodityType;
        PaymentType = "8";
        PaymentLinkType = "1";
        NotifyType = "1";
        ResultNotifyURL = resultNotifyURL;
        MerchantRemarks = AssertUtils.checkEmptyBack(merchantRemarks);
        IsBreakAccount = "0";
    }
}
