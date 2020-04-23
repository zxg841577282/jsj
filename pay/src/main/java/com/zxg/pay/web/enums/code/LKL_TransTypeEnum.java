package com.zxg.pay.web.enums.code;

/**
 * @Author: zhou_xg
 * @Date: 2019/11/15 14:00
 * @Purpose: 拉卡拉接入方式枚举
 */

public enum LKL_TransTypeEnum {
    NATIVE("NATIVE","41"),
    JSAPI("JSAPI","51")
    ;

    private String info;

    private String code;

    public String getInfo() {
        return info;
    }

    public String getCode() {
        return code;
    }

    LKL_TransTypeEnum(String info, String code) {
        this.info = info;
        this.code = code;
    }
}
