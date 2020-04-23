package com.zxg.pay.web.enums.code;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhou_xg
 * @Date: 2019/12/2 16:32
 * @Purpose: 经营内容
 */

public enum  BizContentEnum {
    type_10001(10001,"餐饮、宾馆、娱乐、珠宝金饰、工艺美术品"),
    type_10002(10002,"房地产汽车类"),
    type_10003(10003,"百货、中介、培训、景区门票等"),
    type_10004(10004,"批发类商户"),
    type_10005(10005,"加油、超市类"),
    type_10006(10006,"交通运输售票"),
    type_10007(10007,"水电气缴费"),
    type_10008(10008,"政府类"),
    type_10009(10009,"便民类"),
    type_10010(10010,"公立医院、公立学校、慈善"),
    type_10011(10011,"宾馆餐饮娱乐类"),
    type_10012(10012,"房产汽车类"),
    type_10013(10013,"批发类"),
    type_10014(10014,"超市加油类"),
    type_10015(10015,"一般类商户"),
    type_10016(10016,"三农商户"),
    ;


    private Integer code;

    private String value;

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    BizContentEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static List getList(){
        List list = Lists.newArrayList();//Lists.newArrayList()其实和new ArrayList()几乎一模

        for (BizContentEnum airlineTypeEnum : BizContentEnum.values()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", airlineTypeEnum.getCode());
            map.put("name", airlineTypeEnum.getValue());
            list.add(map);
        }
        return list;
    }
}
