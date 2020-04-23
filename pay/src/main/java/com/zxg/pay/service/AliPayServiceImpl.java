//package com.zxg.pay.service;
//
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.domain.AlipayTradePayModel;
//import com.alipay.api.domain.AlipayTradeRefundModel;
//import com.alipay.api.domain.AlipayTradeWapPayModel;
//import com.alipay.api.request.AlipayTradePayRequest;
//import com.alipay.api.request.AlipayTradeRefundRequest;
//import com.alipay.api.request.AlipayTradeWapPayRequest;
//import com.alipay.api.response.AlipayTradePayResponse;
//import com.alipay.api.response.AlipayTradeRefundResponse;
//import com.zxg.pay.config.AlipayConfig;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * @Author: zhou_xg
// * @Date: 2020/4/22
// * @Purpose:
// */
//@Service
//public class AliPayServiceImpl implements AliPayService{
//
//    public static String APPID = "2016092700610515";
//
//    public static String RSA_PRIVATE_KEY = "MIIEpQIBAAKCAQEAr1i8RxLxtCwlGBpnNbbxyT2pD9aMz7/VXpoIuQVlG+yQzIOrqm6f5/VKf5BgBoxn7+ZxvRMBVAAkyq2iI+QEimrsD98orXngrfPFDqsoNXGYzLx9TVB6PlHp+tnNQS7TMNPoavwH4tsGBA3wV1hJP6WhWGbT9q1b4yHaOzG7ZkT4N1fhQCwhRyCD5J9wyY8FyUOdu1JDEWLNefu8DBTFs0aXeiWA1Sr0mrGFIvSJWEuVjGnVVRFkMTb/ByPMBROdVkDA7STSeJm9kqjWLvS4Zc/zdJrKcaIZInqoqQ/4jocdGTx6sN+9Dt0iABozLh7KyWWweMOSk8fctLkr5dasjwIDAQABAoIBADmjmqhmYsuOI1dr /ndavtofCb7qEPmNnq7tRWbEjjsCpYqE0d5BZRETwbxzEg21A8mJX+P+JladgXqaXw0bPB7gDN8exTlZMfEci69Jtfrzb34Fi87pYHWx6Y8LH3yIzuVjEyhv1ovM4Ypdpp6fp9xjLslna8/Ppr/f9eO1cluoTsWnixdQ8dIkC+/WB0m2nzIlvz6mr4hnPrQaLQzch1nhEIOLsxBUMLUDUHxm2TwPptrzZCbpd1rx/g/uJZEclt+B4NC9s9x6Uev4jDbQbeGR8cu5wjYO/ivImp5G0uNGKqwfxGeMRPGULJXRZlA4WbrVmJLZ2gHKc91hOLdTahkCgYEA4HPZ95fuPG+vywPX10w83eszz6DsDrJBOLkFMYYz3EFkCamsYZFOxuDFdTsZ3Hsvoh34WldQ0/+0xborRcHsYsrlhZz4FyQ9unsYBbhSCFkPSCKRTuihCz078cgg0uxrZQcWjq3Aa1tpSLpFZvPnFoHYurWTWG+mGUgxa5/f+TMCgYEAx/36LbCzwwhmlbqZbovzQesM/vlaVMCWiMuNa6BMyP+0xnFGMgErIVdvDoTKslfK9LgzDoSGM55IkP/q5oBZk/nrtDSimyXMgXRkSSUe9DxVxoBT0x3ZzSJ4TrlGTvMy2zTCOjY8ZDqNJWiaCKJgv9n+1Pu2NkV+llRDhDuZlzUCgYEAtOYqgpb899C8Sk9qoqdbVzC9rVXxa1bXojPcr7GOErLRQchymqBWcYuvGXDswvq1xV+KTqWZC4RH2iTVw38sWeNVYX7FpUEcTzPuhI2t7/R3kJfrrUFoqnpL2e6GJflOBTrVP2FieCRQksEuNm/VjBpS5wh5HQm/QRkiW6+bAGkCgYEAho9VB+7vVFdPpoiReXbCamS7LscZRxL5dfr3ISYO4+JggHBwmW591YYdm/gu5YX6cWOKPsdeAFcMXjtCkmpjWxP5yhrsGVV6XtOwMiK+y/fYgqGPROm9xK7foaA5NR+e5Sx/Tml1gr5+f1/827hdPlhvhGAD/koN3Rm+ /7aWJc0CgYEAvA+mHMszIl2+OK2IiQxu14eY0XVOg6FLDLQ5m3TWc8sRw3yAFybij1zRKmVKNmOkaWBh2NsgE5MDe28xbAj/5YpTYBgyt36RAdnaFqnwR1WoToXctxZH0GM2jXztOo1puMfthGSEobWPwzPZdnqcN1LTFeGWDiXBcpgsaOc7PWE=";
//
//    public static String notify_url = "notify_domain";
//
//    public static String return_url = "serverUrl";
//    // 请求网关地址
//    public static String URL = "https://openapi.alipay.com/gateway.do";
//    // 编码
//    public static String CHARSET = "UTF-8";
//    // 返回格式
//    public static String FORMAT = "json";
//    // 支付宝公钥
//    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAygx8ShV7XDs8ds6l4rEZRVS1DrfQeii7fSOOenp8uEZSWvJQMtfLMx/2FQtcBIV1egnC9sBgdhX9P1044nsUZN04Ri94tU5lhONTpxp7KkBTgz5ann1wDmWHP6bVZavM14TwDCdd/kY3aPOf6UW0DjhAxINQ309l8fpYXDxwDoskcuCBVTdMVzdfPh0DAMRLY7FuWHnhkDi8yrb40Pr/YbnE1XNQyAQ/4RbqXmFMtK1LO2TsKF/ZevXutEqh4i7s4MdACYXKoGz6Ui78/35gnPdHkoN9eJYpokyY0nJDlX9+kTuyysW8tcThc9rJUmm3PgHffi5SD+VgG0DZnGAOYQIDAQAB";
//    // 日志记录目录
//    public static String log_path = "/log";
//    // RSA2
//    public static String SIGNTYPE = "RSA2";
//
//
//    //条码支付
//    public static Boolean barCodePay (String authCode,String orderNo, String orderName, String fee) throws AlipayApiException {
//        AlipayClient client = new DefaultAlipayClient(URL, APPID, RSA_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY,SIGNTYPE);
//        AlipayTradePayRequest request = new AlipayTradePayRequest();
//        AlipayTradePayModel other = new AlipayTradePayModel();
//        request.setBizModel(other);
//        other.setOutTradeNo(orderNo);
//        other.setScene("bar_code");
//        other.setAuthCode(authCode);
//        other.setSubject(orderName);
//        other.setTotalAmount(fee);
//        other.setBody("订单描述");
//        AlipayTradePayResponse response = client.execute(request);
//        if(response.isSuccess()){
//            System.out.println("调用成功");
//            System.out.println(response.getBody());
//            return true;
//        } else {
//            System.out.println("调用失败");
//            System.out.println(response.getBody());
//            return false;
//        }
//    }
//
//    //支付
//    public void pay (String orderNo, HttpServletRequest request, HttpServletResponse response){
//        if(request.getParameter("WIDout_trade_no")!=null){
//            OrdOrderEntity ordOrder = ordOrderService.selectById(orderNo);
//
//            // 商户订单号，商户网站订单系统中唯一订单号，必填
//            String out_trade_no = ordOrder.getId();
//            // 订单名称，必填
//            String subject = ordOrder.getId();
//            // 付款金额，必填
//            String total_amount= String.valueOf(ordOrder.getTotleAmount());
//            // 商品描述，可空
//            String body = "商品描述";
//            // 超时时间 可空
//            String timeout_express="2m";
//            // 销售产品码 必填
//            String product_code="QUICK_WAP_WAY";
//            /**********************/
//            // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
//            //调用RSA签名方式
//            AlipayClient client = new DefaultAlipayClient(URL, APPID, RSA_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY,SIGNTYPE);
//            AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();
//
//            // 封装请求支付信息
//            AlipayTradeWapPayModel other=new AlipayTradeWapPayModel();
//            other.setOutTradeNo(out_trade_no);
//            other.setSubject(subject);
//            other.setTotalAmount(total_amount);
//            other.setBody(body);
//            other.setTimeoutExpress(timeout_express);
//            other.setProductCode(product_code);
//            alipay_request.setBizModel(other);
//            // 设置异步通知地址
////            alipay_request.setNotifyUrl(AlipayConfig.notify_url);
//            // 设置同步地址
////            alipay_request.setReturnUrl(AlipayConfig.return_url);
//
//            // form表单生产
//            String form = "";
//            try {
//                // 调用SDK生成表单
//                form = client.pageExecute(alipay_request).getBody();
//                response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
//                response.getWriter().write(form);//直接将完整的表单html输出到页面
//                response.getWriter().flush();
//                response.getWriter().close();
//            } catch (AlipayApiException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    //退款
//    public void refund (String orderNo, HttpServletRequest request, HttpServletResponse response){
//        if(request.getParameter("WIDout_trade_no")!=null||request.getParameter("WIDtrade_no")!=null){
//            OrdOrderEntity ordOrder = ordOrderService.selectById(orderNo);
//
//            // 商户订单号，商户网站订单系统中唯一订单号，必填
//            String out_trade_no = ordOrder.getId();
//            // 付款金额，必填
//            String refund_amount= String.valueOf(ordOrder.getTotleAmount());
//            //退款的原因说明
//            String refund_reason = new String("退款原因");
//            //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
//            String  out_request_no = new String(getStringRandom(32));
//
//            // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
//            //调用RSA签名方式
//            AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
//            AlipayTradeRefundRequest alipay_request = new AlipayTradeRefundRequest();
//
//            AlipayTradeRefundModel other=new AlipayTradeRefundModel();
//            other.setOutTradeNo(out_trade_no);
//            other.setRefundAmount(refund_amount);
//            other.setRefundReason(refund_reason);
//            other.setOutRequestNo(out_request_no);
//            alipay_request.setBizModel(other);
//
//            AlipayTradeRefundResponse alipay_response = null;
//            try {
//                alipay_response = client.execute(alipay_request);
//            } catch (AlipayApiException e) {
//                e.printStackTrace();
//            }
//            System.out.println(alipay_response.getBody());
//        }
//    }
//
//}
