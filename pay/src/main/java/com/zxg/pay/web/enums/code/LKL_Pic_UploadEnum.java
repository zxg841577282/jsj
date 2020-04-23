package com.zxg.pay.web.enums.code;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhou_xg
 * @Date: 2019/12/2 10:08
 * @Purpose: 附件上传
 */

public enum LKL_Pic_UploadEnum {

    ID_CARD_FRONT("ID_CARD_FRONT", "身份证正面"),
    ID_CARD_BEHIND("ID_CARD_BEHIND", "身份证反面"),
    BANK_CARD("BANK_CARD", "银行卡"),
    BUSINESS_LICENCE("BUSINESS_LICENCE", "营业执照"),
    PERSONAL_PHOTO("PERSONAL_PHOTO", "合影照片"),
    MERCHANT_PHOTO("MERCHANT_PHOTO", "商户照片"),
    OTHERS("OTHERS", "其他")
    ;

    private String value;
    private String msg;

    LKL_Pic_UploadEnum(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public String getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }

    public static List getList(){
        List list = Lists.newArrayList();//Lists.newArrayList()其实和new ArrayList()几乎一模

        for (LKL_Pic_UploadEnum airlineTypeEnum : LKL_Pic_UploadEnum.values()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", airlineTypeEnum.getValue());
            map.put("name", airlineTypeEnum.getMsg());
            list.add(map);
        }
        return list;
    }
}
