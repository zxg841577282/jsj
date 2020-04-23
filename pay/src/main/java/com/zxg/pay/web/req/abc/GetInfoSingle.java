package com.zxg.pay.web.req.abc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import other.ResultException;

/**
 * @Author: zhou_xg
 * @Date: 2020/3/12 16:14
 * @Purpose:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetInfoSingle {
    /**
     * ImmediatePay：直接支付
     * PreAuthPay：预授权支付
     * DividedPay：分期支付
     * AgentPay：授权支付
     * Refund：退款
     * DefrayPay：付款
     * PreAuthed：预授权确认
     * PreAuthCancel：预授权取消
     */
    private String PayTypeID;//必需 交易类型

    private String OrderNo;//必需 交易编号
    /**
     * 0：状态查询
     * 1：详细查询
     */
    private String QueryDetail;//必需 是否查询详细信息

    public String getPayTypeID() {
        if (PayTypeID == null){ throw new ResultException("交易类型不能为空");}
        return PayTypeID;
    }

    public String getOrderNo() {
        if (PayTypeID == null){ throw new ResultException("交易编号不能为空");}
        return OrderNo;
    }

    public String getQueryDetail() {
        if (PayTypeID == null){ throw new ResultException("是否查询详细信息不能为空");}
        return QueryDetail;
    }
}
