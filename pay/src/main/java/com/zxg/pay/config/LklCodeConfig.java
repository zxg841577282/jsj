package com.zxg.pay.config;

/**
 * @Author: zhou_xg
 * @Date: 2019/11/15 13:58
 * @Purpose: 拉卡拉商户信息
 */

public class LklCodeConfig {

    public static String shopName = "蜘蛛王";//显示在支付界面的名称

    public static String shopNo = "";//拉卡拉分配的商户号

    public static String termId = "";//拉卡拉分配的终端号

    public static String Url = "https://ipos.lakala.com/q/api/mch/cmd";         //生产环境URL

    public static String notifyUrl = "http://zb.chinazzw.com/zzw-ht/lkl/payBack/lklQRCodePayBack";//回调通知地址

    public static String lklDynamicNotifyUrl = "http://jx.chinazzw.com/ht/lkl/payBack/lklDynamicQRCodePayBack";//动态二维码回调通知地址

    /*--------------------以下为测试环境参数-------------------------*/

    public static String testUrl = "https://test.wsmsd.cn/q/api/mch/cmd";       //测试环境URL

    public static String testAPPID = "qm1624980136";                            //测试环境APPID

    public static String testTOKEN = "43e99b4a38a748d3932fca9382404b41";        //测试环境TOKEN

    public static String testSecret_Key = "f6cc7030b8c0ba07a6da488362f2748e";   //测试环境secret_Key

    public static String testUser = "822126090640003";                         //测试环境商户号A

    public static String testNumber = "47781282";                              //测试环境终端号A
}
