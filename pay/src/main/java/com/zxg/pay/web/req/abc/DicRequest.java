package com.zxg.pay.web.req.abc;

import lombok.Data;
import lombok.NoArgsConstructor;
import other.ResultException;

import javax.validation.constraints.NotNull;

/**
 * @Author: zhou_xg
 * @Date: 2020/3/10 17:33
 * @Purpose:
 */
@Data
@NoArgsConstructor
public class DicRequest {
    /**
     * 1：农行借记卡支付
     * 2：国际卡支付
     * 3：农行贷记卡支付
     * 5：B2B 跨行支付
     * 6：银联跨行支付
     * 7：农行对公账户支付 (含银联跨行 B2B 支付）
     * A:支付方式合并（包括 1 和 3）
     */
    private String PaymentType;//必需 支付类型
    /**
     * 1：internet 网络接入
     * 2：手机网络接入
     * 3：数字电视网络接入
     * 4：智能客户端
     */
    private String PaymentLinkType;//必需 交易渠道
    private String UnionPayLinkType;//银联跨行移动支付接入方式 非必须，
    // 但是如果选择的支付帐户类型为 6(银联跨行支付)交易渠道为 2(手机网络接入)，必须设定 0：页面接入 1：客户端接入（仅支持方式一，方式二不支持）

    private String ReceiveAccount;//收款方账号
    private String ReceiveAccName;//收款方户名
    /**
     * 0：URL 页面通知
     * 1：服务器通知
     */
    private String NotifyType;//必需 通知方式

    @NotNull(message = "通知 URL 地址不能为空")
    private String ResultNotifyURL;//必需 通知 URL 地址  0：URL 页面通知  1：服务器通知
    private String MerchantRemarks;//附言
    private String ReceiveMark;//交易是否直接入二级商户账户 0:否  1:是
    private String ReceiveMerchantType;//收款方账户类型,  0:个人 1:对公
    private String IsBreakAccount;//必需 交易是否分账；是否支持向二级商户入账 0:否 1:是（资金分账商户必须填 1）
    /**
     * 0:否
     * 1:是（资金分账商户必须填 1）
     */
    private String SplitAccTemplate;//分账模版编号


    public DicRequest(String resultNotifyURL) {
        PaymentType = "A";
        PaymentLinkType = "1";
        UnionPayLinkType = "";
        ReceiveAccount = "";
        ReceiveAccName = "";
        NotifyType = "1";
        ResultNotifyURL = resultNotifyURL;
        MerchantRemarks = "";
        ReceiveMark = "";
        ReceiveMerchantType = "";
        IsBreakAccount = "0";
        SplitAccTemplate = "";
    }

    public String getUnionPayLinkType() {
        if (PaymentType.equals("6") && PaymentLinkType.equals("2") && UnionPayLinkType == null){
            throw new ResultException("是如果选择的支付帐户类型为 6(银联跨行支付)交易渠道为 2(手机网络接入)交易渠道时，不能为空");}
        return UnionPayLinkType = UnionPayLinkType==null?"":UnionPayLinkType;
    }
}
