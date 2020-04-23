package com.zxg.pay.web.req.wx;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/21
 * @Purpose:
 */
@Getter
@Setter
public class BaseWxInfoIM {

    //小程序/公众号的APPID
    @NotNull(message = "APPID不能为NULL")
    private String appid;

    //商户号
    @NotNull(message = "商户号不能为NULL")
    private String mch_id;

    //商户密钥，加密使用
    @NotNull(message = "商户密钥不能为NULL")
    private String mchidKey;

    public BaseWxInfoIM(@NotNull(message = "APPID不能为NULL") String appid, @NotNull(message = "商户号不能为NULL") String mch_id, @NotNull(message = "商户密钥不能为NULL") String mchidKey) {
        this.appid = appid;
        this.mch_id = mch_id;
        this.mchidKey = mchidKey;
    }
}
