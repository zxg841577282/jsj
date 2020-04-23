//package com.zxg.pay.web.controller.lakala.code.demo;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.sig.backoffice.common.exception.RRException;
//import com.sig.backoffice.util.LKL.code.enums.MerchantBaseEnum;
//import com.sig.backoffice.util.LKL.code.pojo.FormFieldKeyValuePair;
//import com.sig.backoffice.util.LKL.code.pojo.MercRegister;
//import com.sig.backoffice.util.LKL.code.utils.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @Author: zhou_xg
// * @Date: 2019/11/29 9:43
// * @Purpose: 拉卡拉扫码支付商户进件
// */
//
//public class LKL_Code_Register_Merchant {
//
//    public static void main(String[] args) {
//        try {
////            MercRegister(OrderNumUtil.makeOrderNum("s"));
//
////            MercRegisterSearch("531336");
//
////            terminalRegister("822331072780006");
//
////            MercSearch("822331072780006");
//
////            test("531241","","822290059460618");
//
////            picUpload("525691",LKL_Pic_UploadEnum.BANK_CARD,"","http://zb.chinazzw.com:8080/uploads/zzw/1574910054745.jpg");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 商户进件
//     */
//    public static String MercRegister(MercRegister mercRegister){
//        mercRegister.setMAC(SignUtil.getSha1(mercRegister.getPin()));
//        //map转XML字符串
//        Map<String, Object> map = MapToXmlUtil.convertToMap(mercRegister);
//
//        String xml = MapToXmlUtil.mapToXML(map);
//
//        System.out.println("扫码商户进件请求XML = " + xml);
//        return HttpClientUtil.doPost(MerchantBaseEnum.Url.getValue(),xml);
//    }
//
//    /**
//     *  查询
//     * @param contractId 进件流水号
//     */
//    public static String MercRegisterSearch(String contractId){
//        Map<String,Object> map = new HashMap<>();
//        map.put("funCod","6005");
//        map.put("compOrgCode",MerchantBaseEnum.compOrgCode.getValue());//第三方平台标识
//        map.put("bmcpOrgCode",MerchantBaseEnum.bmcpOrgCode.getValue());//拉卡拉BMCP提供的机构代码注：只有无卡支付应用才需上送
//        map.put("contractId",contractId);//进件ID
//
//        String pin = MerchantBaseEnum.compOrgCode.getValue() + contractId + MerchantBaseEnum.Sercret_Key.getValue();
//        map.put("MAC", SignUtil.getSha1(pin));//签名
//
//        String xml = MapToXmlUtil.mapToXML(map);
//        System.out.println("查询商户进件XML = " + xml);
//        return HttpClientUtil.doPost("https://test.wsmsd.cn/thirdpartplatform/merchmanage/6005.dor", xml);
//    }
//
//    /**
//     * 终端进件
//     */
//    public static String terminalRegister(String shopNo){
//        Map<String,Object> map = new HashMap<>();
//        map.put("funCod","6003");
//        map.put("compOrgCode",MerchantBaseEnum.compOrgCode.getValue());//第三方平台标识
//        map.put("shopNo",shopNo);//商户号
//        map.put("termNum","1");//终端数量
//        map.put("retUrl","1");//通知地址
//
//        String pin = MerchantBaseEnum.compOrgCode.getValue() + shopNo + "1" + MerchantBaseEnum.Sercret_Key.getValue();
//
//        map.put("MAC", SignUtil.getSha1(pin));//签名
//
//        String xml = MapToXmlUtil.mapToXML(map);
//        System.out.println("XML = " + xml);
//        return HttpClientUtil.doPost("https://test.wsmsd.cn/thirdpartplatform/merchmanage/6003.dor", xml);
//    }
//
//    /**
//     * 商户信息查询
//     */
//    public static void MercSearch(String shopNo){
//        Map<String,Object> map = new HashMap<>();
//        map.put("funCod","6006");
//        map.put("compOrgCode",MerchantBaseEnum.compOrgCode.getValue());//第三方平台标识
//        map.put("shopNo",shopNo);//商户号
//
//        String pin = MerchantBaseEnum.compOrgCode.getValue() + MerchantBaseEnum.Sercret_Key.getValue();
//
//        map.put("MAC", SignUtil.getSha1(pin));//签名
//
//        String xml = MapToXmlUtil.mapToXML(map);
//        System.out.println("XML = " + xml);
//        HttpClientUtil.doPost("https://test.wsmsd.cn/thirdpartplatform/merchmanage/6006.dor", xml);
//    }
//
//    /**
//     * 附件上传
//     */
//    public static boolean test(MultipartFile file, String contractId, String shopNo, String code){
//        ArrayList<FormFieldKeyValuePair> ffkvp = new ArrayList<FormFieldKeyValuePair>();
//        ffkvp.add(new FormFieldKeyValuePair("version", "10"));//其他参数
//        ffkvp.add(new FormFieldKeyValuePair("command", "ICP_UPLOAD_ATTACHMENT"));//操作指令
//        ffkvp.add(new FormFieldKeyValuePair("platform","TP"));//平台来源
//        ffkvp.add(new FormFieldKeyValuePair("compOrgCode", MerchantBaseEnum.bmcpOrgCode.getValue()));//平台来源
//        ffkvp.add(new FormFieldKeyValuePair("contractId", contractId));
//        ffkvp.add(new FormFieldKeyValuePair("type", code));
//        ffkvp.add(new FormFieldKeyValuePair("shopNo", shopNo));
//
////        // 设定要上传的文件
////        ArrayList<UploadFileItem> ufi = new ArrayList<UploadFileItem>();
////        ufi.add(new UploadFileItem("jpg", "C:\\Users\\zxg\\Desktop\\timg.jpg"));
//
//        HttpPostEmulator hpe = new HttpPostEmulator();
//        String response = null;
//        try {
//            response = hpe.sendHttpPostRequest(MerchantBaseEnum.PicUrl.getValue(), ffkvp, file);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("Responsefrom server is: " + response);
//
//        JSONObject parse = (JSONObject) JSON.parse(response);
//        if (parse.getString("responseCode").equals("000000")){
//            return true;
//        }else {
//            throw new RRException("附件上传失败：" + parse.getString("message"));
//        }
//
//    }
//}
