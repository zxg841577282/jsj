package com.zxg.pay.web.enums.code;

/**
 * @author : L.T.P
 * @version V1.0.0
 * @Project: lkl-zf-ss-onlinepay-sdk
 * @Package com.lakala.boss.api.demo
 * @Description: 请求地址&商户基本信息
 * @date Date : 2019年10月15日 09:37
 */
public enum MerchantBaseEnum {

    Sercret_Key("4ZBu7cGBDhggQ3f9S1puCUNiLXGLcvqb", "Sercret_Key"),
    Url("https://test.wsmsd.cn/thirdpartplatform/merchmanage/6001.dor", "请求外网地址"),
    PicUrl("http://180.167.225.200:8661/UploadAttachment", "进件附件上传地址"),
    returnUrl("http://gc.chinazzw.com:8080/ht/mem/user/QRCodeRegisterBack", "回调地址"),
    compOrgCode("GJEO", "SIPS机构代码"),
    bmcpOrgCode("230280", "MCP机构代码");


    private String value;
    private String msg;

    MerchantBaseEnum(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
