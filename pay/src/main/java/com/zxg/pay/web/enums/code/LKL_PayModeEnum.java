package com.zxg.pay.web.enums.code;

/**
 * @Author: zhou_xg
 * @Date: 2019/11/15 14:00
 * @Purpose: 拉卡拉支付模式枚举
 */

public enum LKL_PayModeEnum {
    WECHAT("微信", "WECHAT"),
    ALIPAY("支付宝", "ALIPAY"),
    UQRCODEPAY("银联", "UQRCODEPAY "),
    BESTPAY("翼支付", "BESTPAY"),
    SUNING("苏宁", "SUNING");

    private String info;

    private String code;

    public String getInfo() {
        return info;
    }

    public String getCode() {
        return code;
    }

    LKL_PayModeEnum(String info, String code) {
        this.info = info;
        this.code = code;
    }
}
