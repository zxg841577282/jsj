package com.zxg.pay.web.req.lakala.code;

import com.zxg.pay.web.enums.code.MerchantBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhou_xg
 * @Date: 2019/12/2 16:17
 * @Purpose:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MercRegister {

    private String funCod = "6001";

    private String compOrgCode = MerchantBaseEnum.compOrgCode.getValue();

    private String bmcpOrgCode = MerchantBaseEnum.bmcpOrgCode.getValue();

    private String channelType = "TP_MERCHANT";

    private String posType = "WECHAT_PAY";

    private String orderNo = "";

    private String merName;//商户名称

    private String bizName;//商户经营名

    private String merLicenseNo;//营业执照号

    private String provinceCode;//省
    private String cityCode;//市
    private String countyCode;//区
    private String address;//详细地址
    private String bizContent;//商户经营内

    private String crLicenceName;//商户法人姓名
    private String crLicenceNo;//法人身份证
    private String idCardExpire;//法人身份证有效期
    private String contactMobile;//联系人手机号码

    private List<Map<String,String>> wechatFees = getWechat();

    private String openningBankNo;//开户行号
    private String openningBankName;//开户支行名称
    private String clearingBankNo;//清算行号
    private String accountNo;//账户号
    private String accountName;//账户名称
    private String accountKind = "58";//账户性质
    private String idCard;//入账人身份证号码
    private String settlePeriod = "T+1";//结算周期
    private String mccCode;//行业代码
    private String debitRate="0.006";//借记卡手续费
    private String termNum = "1";//终端数量
    private String retUrl = "http://mps.1card1.cn/notify/6001/lakalaAuditNotify";//通知地址

    private String MAC = getPin();

    private static List<Map<String,String>> getWechat(){
        List<Map<String,String>> wechatFees = new ArrayList<>();

        Map<String,String> map = new HashMap<>();
        map.put("wechatType","WECHAT_PAY_FEE");
        map.put("wechatRate","0.6");
        wechatFees.add(map);

        Map<String,String> map2 = new HashMap<>();
        map2.put("wechatType","ALIPAY_WALLET_FEE");
        map2.put("wechatRate","0.6");
        wechatFees.add(map2);

        Map<String,String> map3 = new HashMap<>();
        map3.put("wechatType","UNIONPAY_WALLET_DEBIT_FEE");
        map3.put("wechatRate","0.6");
        wechatFees.add(map3);

        Map<String,String> map4 = new HashMap<>();
        map4.put("wechatType","UNIONPAY_WALLET_CREDIT_FEE");
        map4.put("wechatRate","0.6");
        wechatFees.add(map4);

        Map<String,String> map5 = new HashMap<>();
        map5.put("wechatType","BESTPAY_PURCHASE_FEE");
        map5.put("wechatRate","0.6");
        wechatFees.add(map5);

        return wechatFees;
    }

    public String getPin(){
        return getCompOrgCode() + getChannelType() + getMerLicenseNo() + getProvinceCode() + getCityCode() + getCountyCode() + getCrLicenceNo()
                + getContactMobile() + getOpenningBankNo() + getClearingBankNo() + getAccountNo() + getMccCode() + getTermNum() + MerchantBaseEnum.Sercret_Key.getValue();
    }
}
