//package com.zxg.get_logistics.service.ZOP;
//
//import com.alibaba.fastjson.JSONObject;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.nio.charset.Charset;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Base64Util;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * @Author: zhou_xg
// * @Date: 2019/8/22 0022
// * @Purpose:
// */
//@Service
//public class ZOPClient implements ZOPService {
//
//    /**
//     * 物流轨迹查询
//     * @param expressNo
//     */
//    public String selectTraceInterfaceNewTraces(String expressNo,String companyId,String key) throws IOException, NoSuchAlgorithmException {
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("data", "['"+expressNo+"']");
//        parameters.put("company_id", companyId);
//        parameters.put("msg_type", "NEW_TRACES");
//
//        String strToDigest = paramsToQueryString(parameters) + key;
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        md.update(strToDigest.getBytes(Charset.forName("UTF-8")));
//
//        String dataDigest = Base64Util.getEncoder().encodeToString(md.digest());
//        URL url = new URL("http://japi.zto.cn/traceInterfaceNewTraces");
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
//        con.setDoOutput(true);
//        con.setConnectTimeout(5000);
//        con.setReadTimeout(5000);
//        con.setRequestProperty("x-datadigest", dataDigest);
//        con.setRequestProperty("x-companyid", companyId);
//        DataOutputStream out = new DataOutputStream(con.getOutputStream());
//        out.write(paramsToQueryStringUrlencoded(parameters).getBytes(Charset.forName("UTF-8")));
//        out.flush();
//        out.close();
//        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuilder content = new StringBuilder();
//        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//        in.close();
//        System.out.println(content.toString());
//        return content.toString();
//    }
//
//    /**
//     * 获取电子面单
//     */
//    public void AcquisitionOfElectronicSurfaceSheets(Map<String,String> map,String companyId,String key,String DZMDID) throws IOException, NoSuchAlgorithmException {
//
//        String expressNo = map.get("orderNo");
////        String expressNo = "xfs101100111011";
//
//        //寄件人信息
//        String senderName = map.get("senderName");
//        String senderMobile = map.get("senderMobile");
//        String senderCity = map.get("senderCity");
//        String senderAddress = map.get("senderAddress");
//
//        //收件人信息
//        String receiverName = map.get("receiverName");
//        String receiverMobile = map.get("receiverMobile");
//        String receiverCity = map.get("receiverCity");
//        String receiverAddress = map.get("receiverAddress");
//        //其他
//        String freight = map.get("freight");
//        String order_sum = map.get("order_sum");
//        String remark = map.get("remark");
//
//
//        Map<String, String> parameters = new HashMap<>();
//
//        parameters.put("company_id", companyId);
//        parameters.put("msg_type", "submitAgent");
//        parameters.put("data", "{\n" +
//                "\t\"partner\": \""+DZMDID+"\",\n" +
//                "\t\"id\": \""+expressNo+"\",\n" +
//                "\t\"type\": \"\",\n" +
//                "\t\"sender\": {\n" +
//                "\t\t\"name\": \""+senderName+"\",\n" +
//                "\t\t\"mobile\": \""+senderMobile+"\",\n" +
//                "\t\t\"city\": \""+senderCity+"\",\n" +
//                "\t\t\"address\": \""+senderAddress+"\"\n" +
//                "\t},\n" +
//                "\t\"receiver\": {\n" +
//                "\t\t\"name\": \""+receiverName+"\",\n" +
//                "\t\t\"mobile\": \""+receiverMobile+"\",\n" +
//                "\t\t\"city\": \""+receiverCity+"\",\n" +
//                "\t\t\"address\": \""+receiverAddress+"\"\n" +
//                "\t},\n" +
//                "\t\"freight\": \""+freight+"\",\n" +
//                "\t\"order_sum\": \""+order_sum+"\",\n" +
//                "\t\"collect_moneytype\": \"CNY\",\n" +
//                "\t\"remark\": \""+remark+"\",\n" +
//                "\t\"order_type\": \"0\"\n" +
//                "}");
//
//
//        String strToDigest = paramsToQueryString(parameters) + key;
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        md.update(strToDigest.getBytes(Charset.forName("UTF-8")));
//
//        String dataDigest = Base64Util.getEncoder().encodeToString(md.digest());
//        URL url = new URL("http://japi.zto.cn/partnerInsertSubmitagent");
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
//        con.setDoOutput(true);
//        con.setConnectTimeout(5000);
//        con.setReadTimeout(5000);
//        con.setRequestProperty("x-datadigest", dataDigest);
//        con.setRequestProperty("x-companyid", companyId);
//        DataOutputStream out = new DataOutputStream(con.getOutputStream());
//        out.write(paramsToQueryStringUrlencoded(parameters).getBytes(Charset.forName("UTF-8")));
//        out.flush();
//        out.close();
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuilder content = new StringBuilder();
//        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//        in.close();
//        String data1 = content.toString();
//        System.out.println(data1);
//        JSONObject jsonObject = MapJsonStringUtil.StringToJson(data1);
//        String result = String.valueOf(jsonObject.get("result"));
//        JSONObject data2 = MapJsonStringUtil.StringToJson(String.valueOf(jsonObject.get("data")));
//        //判断是否成功
//        if (result == "false"){
//            String message = String.valueOf(data2.get("message"));
//            return R.error(message);
//        }
//        String expNo = String.valueOf(data2.get("orderId"));
//        Map<String,String> map1 = new HashMap<>();
//        map1.put("expNo",expNo);
//        map1.put("expCom","中通快递股份有限公司");
//        return R.ok(map1);
//    }
//
//    /**
//     * 云打印
//     */
//    public R CloudPrinting(Map<String,String> map,String companyId,String key) throws IOException, NoSuchAlgorithmException {
//
//        String expressNo = map.get("orderNo");
////        String expressNo = "xfs101100111011";
//
//        //寄件人信息
//        String senderName = map.get("senderName");
//        String senderMobile = map.get("senderMobile");
//        String senderCity = map.get("senderCity");
//        String senderProv = map.get("senderProv");
//        String senderCounty = map.get("senderCounty");
//        String senderAddress = map.get("senderAddress");
//
//        //收件人信息
//        String receiverName = map.get("receiverName");
//        String receiverMobile = map.get("receiverMobile");
//        String receiverCity = map.get("receiverCity");
//        String receiverProv = map.get("receiverProv");
//        String receiverCounty = map.get("receiverCounty");
//        String receiverAddress = map.get("receiverAddress");
//
//
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("request", "{\n" +
//                "\t\"partnerCode\": \""+expressNo+"\",\n" +
//                "\t\"printChannel\": \"ZOP\",\n" +
//                "\t\"qrcodeId\": \"qrcodeId\",\n" +
//                "\t\"printType\": \"REMOTE_EPRINT\",\n" +
//                "\t\"printerId\": \"NPIF4B39A\",\n" +
//                "\t\"printParam\": {\n" +
//                "\t\t\"elecAccount\": \"test\",\n" +
//                "\t\t\"elecPwd\": \"ZTO123\",\n" +
//                "\t\t\"paramType\": \"NOELEC_MARK\",\n" +
//                "\t\t\"printBagaddr\": \"南京\",\n" +
//                "\t\t\"printMark\": \"230-\"\n" +
//                "\t},\n" +
//                "\t\"receiver\": {\n" +
//                "\t\t\"address\": \""+receiverAddress+"\",\n" +
//                "\t\t\"city\": \""+receiverCity+"\",\n" +
//                "\t\t\"county\": \""+receiverCounty+"\",\n" +
//                "\t\t\"mobile\": \""+receiverMobile+"\",\n" +
//                "\t\t\"name\": \""+receiverName+"\",\n" +
//                "\t\t\"prov\": \""+receiverProv+"\"\n" +
//                "\t},\n" +
//                "\t\"sender\": {\n" +
//                "\t\t\"address\": \""+senderAddress+"\",\n" +
//                "\t\t\"city\": \""+senderCity+"\",\n" +
//                "\t\t\"county\": \""+senderCounty+"\",\n" +
//                "\t\t\"mobile\": \""+senderMobile+"\",\n" +
//                "\t\t\"name\": \""+senderName+"\",\n" +
//                "\t\t\"prov\": \""+senderProv+"\"\n" +
//                "\t}\n" +
//                "}");
//
//
//        String strToDigest = paramsToQueryString(parameters) + key;
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        md.update(strToDigest.getBytes(Charset.forName("UTF-8")));
//
//        String dataDigest = Base64Util.getEncoder().encodeToString(md.digest());
//        URL url = new URL("http://japi.zto.cn/doPrint");
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
//        con.setDoOutput(true);
//        con.setConnectTimeout(5000);
//        con.setReadTimeout(5000);
//        con.setRequestProperty("x-datadigest", dataDigest);
//        con.setRequestProperty("x-companyid", companyId);
//        DataOutputStream out = new DataOutputStream(con.getOutputStream());
//        out.write(paramsToQueryStringUrlencoded(parameters).getBytes(Charset.forName("UTF-8")));
//        out.flush();
//        out.close();
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuilder content = new StringBuilder();
//        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//        in.close();
//        String data1 = content.toString();
//        System.out.println(data1);
//        JSONObject jsonObject = MapJsonStringUtil.StringToJson(data1);
//        String result = String.valueOf(jsonObject.get("result"));
////        JSONObject data2 = JSONObject.fromObject(jsonObject.get("data"));
//        String message = String.valueOf(jsonObject.get("message"));
//        if (!result.equals("true")){
//            return R.error(message);
//        }
//
//
//        return R.ok();
//    }
//
//    /**
//     * 查询账户余额
//     */
//    public R SelectAmount(String companyId,String key,String DZMDID) throws IOException, NoSuchAlgorithmException {
//
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("company_id", companyId);
//        parameters.put("msg_type", "availableCounter");
//        parameters.put("data", "{\n" +
//                "\t\"partner\": \""+DZMDID+"\",\n" +
//                "\t\"lastno\": \"\",\n" +
//                "\t\"typeid\": \"1\",\n" +
//                "\t\"isgettypecount\": \"true\"\n" +
//                "}");
//
//
//        String strToDigest = paramsToQueryString(parameters) + key;
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        md.update(strToDigest.getBytes(Charset.forName("UTF-8")));
//
//        String dataDigest = Base64Util.getEncoder().encodeToString(md.digest());
//        URL url = new URL("http://japi.zto.cn/partnerInsertAvailablecounter");
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
//        con.setDoOutput(true);
//        con.setConnectTimeout(5000);
//        con.setReadTimeout(5000);
//        con.setRequestProperty("x-datadigest", dataDigest);
//        con.setRequestProperty("x-companyid", companyId);
//        DataOutputStream out = new DataOutputStream(con.getOutputStream());
//        out.write(paramsToQueryStringUrlencoded(parameters).getBytes(Charset.forName("UTF-8")));
//        out.flush();
//        out.close();
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuilder content = new StringBuilder();
//        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//        in.close();
//        String data1 = content.toString();
//        System.out.println(data1);
//        JSONObject jsonObject = MapJsonStringUtil.StringToJson(data1);
//        String result = String.valueOf(jsonObject.get("result"));
//        JSONObject data2 = MapJsonStringUtil.StringToJson(String.valueOf(jsonObject.get("data")));
//        //判断是否成功
//        if (result.equals("false")){
//            String message = String.valueOf(data2.get("message"));
//            return R.error(message);
//        }
//        String available = String.valueOf(data2.get("available"));
//        Map<String,String> map1 = new HashMap<>();
//        map1.put("amount",available);
//        return R.ok(map1);
//    }
//
//
//    private static String paramsToQueryString(Map<String, String> params) {
//        return params.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
//    }
//
//    private static String paramsToQueryStringUrlencoded(Map<String, String> params) {
//        return params.entrySet().stream().map(e -> {
//            try {
//                return e.getKey() + "=" + URLEncoder.encode(e.getValue(), "UTF-8");
//            } catch (UnsupportedEncodingException e1) {
//                return e.getValue();
//            }
//        }).collect(Collectors.joining("&"));
//    }
//
//}
