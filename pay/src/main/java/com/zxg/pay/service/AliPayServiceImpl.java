package com.zxg.pay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.zxg.pay.config.AlipayConfig;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static util.RandomNumber.getStringRandom;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/22
 * @Purpose:
 */
@Service
public class AliPayServiceImpl implements AliPayService{

    public static String APPID = "2016092700610515";

    public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCccQT3Nq0EnIVv7GifRar7b7/KcDPegNgokIFWyRGp7oOD+6HJ65Ns0UCV9dQkICWgUpJTiU71CXsTNZ6Tf8J+NDIDyckp7TA56H6flN+qbxlorOzOn+yE5FuBfeCfhjbh9ww6MMY8jy7HPvBdzhgpA/glhJbwVhfrG5Jgayr8ZA3lN6ZlYKe4vc0c5AEL85j6LWzfBAD+6T/Ym/q8u88V4wtm+JF5RC0qM4s7FXqEAIVQhiiLq7PBdmFUPqOy9kq3Q8107TCEXOh5CEf3zpFW+efYkV4BW5RuqlAzHjksmRkaT5xJirfjrGhJ3EfolHp5Xn6odajn67XQtZaYVX7xAgMBAAECggEAWgJg9AfeVnfkAniH3ZPNA6vRMp2KCmwhcn1qHU/EvPwBaU+/T5keEo721RFCxKJGY87+Z9fpuOyChpPkMENi2RAY8rsH5fqVM8xEBnc2lS4k9WgBPAUhpXhp+cA+HCqxqyNkQprLhA5xwKvCyoYIupiRDq0R1ygBzQDpIFaovdu8XLhtYjm7CVrg7jy4cajshQSOwEQA+5Ao+JYMObbpQcBSyriN7kuqZKbSHtYvlliXV/ij4+5vWV1+onS0wbBg+sC/YueEJCzvLjxo3t/5e9hO0NlpezNRCDiczbsSuiXA2F7ngUPJ94wuzcFwcTHwX7ZIDf8oLUF4NNN0J1/30QKBgQD1ScI3gcWTQY+EntrFdjDQeVF7NE8kYqYVTj/pYRJ4eVKtmTKaAcNuwg6hNCvh2hYHJhyDgTojSX09aT5JboB++R6wbch8Yc1W4vcsOeHNIhiraWMeyLijTqeGIzORow+/OdjVw8rjfnkyY6yEoUyt53BsA2ZSVWJtxiRd6kByjQKBgQCjRfv3kIvJ9dxiG26m3zsiTC5p871OSqKM+MydDdYIIX4J6NNs2ll2sMDNZHxedNY+J0M5wQ6aaonYWQuIcCJTNOIKwrPD4Zgzvq67dmNifbBC2xYkoW+RBvYQ8h6kmJW8D619i1d1NyJl62YmUJl0o4lkBl/udRZPZFfRpf7W9QKBgQDO/Rcv0AryYPjDaWokB8DZrAbeeR3wDm8ou4ejYY88quTiKc1RuPs6k/fUgTomtOqxza9yKbLt5C4TjKRLQzyb6Y3ERGBS1hV5pL4ATDgH6yX2QFCswaVN6inkQABcfcXkEOSyPy8PGsMbipEiuxsCBbQQ8i1+fR86k2JM/ki0YQKBgFX6UYABVwtn88CZ73/iR5J9CWfz2Tk0JjsT/TOrCI1JjcSMHWHR7ZCJ7U17X96hqfwa0tTD6/u2pJYP9fnICttEGtBNzToWPv7op7zBz/4ab8oU8qkhdc2qG8O6rBZC336jxtk4mjp4aR3Agw8kHFsGjx/kF3pLoA74qSIWRodNAoGAL8Ps5pK9LSqteYP/IbiRPIyU/xUN7744Jo2qvEPL3ZD7172cbafXJimvIxYQvgrKoVtv/RcH219950nlhB74ZlI2YYwzqCqiJjR44hyg6BLJbCGsnI2gtscG+A45ho32sKLIyNXXxKr+TDj6iWyJaiZwVDfFkxCd/U0d4zplyFY=";

    public static String notify_url = "notify_domain";

    public static String return_url = "serverUrl";
    // 请求网关地址
    public static String URL = "https://openapi.alipay.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAygx8ShV7XDs8ds6l4rEZRVS1DrfQeii7fSOOenp8uEZSWvJQMtfLMx/2FQtcBIV1egnC9sBgdhX9P1044nsUZN04Ri94tU5lhONTpxp7KkBTgz5ann1wDmWHP6bVZavM14TwDCdd/kY3aPOf6UW0DjhAxINQ309l8fpYXDxwDoskcuCBVTdMVzdfPh0DAMRLY7FuWHnhkDi8yrb40Pr/YbnE1XNQyAQ/4RbqXmFMtK1LO2TsKF/ZevXutEqh4i7s4MdACYXKoGz6Ui78/35gnPdHkoN9eJYpokyY0nJDlX9+kTuyysW8tcThc9rJUmm3PgHffi5SD+VgG0DZnGAOYQIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";


    //条码支付
    public static Boolean barCodePay (String authCode,String orderNo, String orderName, String fee) throws AlipayApiException {
        AlipayClient client = new DefaultAlipayClient(URL, APPID, RSA_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY,SIGNTYPE);
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        AlipayTradePayModel other = new AlipayTradePayModel();
        request.setBizModel(other);
        other.setOutTradeNo(orderNo);
        other.setScene("bar_code");
        other.setAuthCode(authCode);
        other.setSubject(orderName);
        other.setTotalAmount(fee);
        other.setBody("订单描述");
        AlipayTradePayResponse response = client.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
            System.out.println(response.getBody());
            return true;
        } else {
            System.out.println("调用失败");
            System.out.println(response.getBody());
            return false;
        }
    }

    //支付
    public void pay (String orderNo,String totalAmount, HttpServletRequest request, HttpServletResponse response){
        if(request.getParameter("WIDout_trade_no")!=null){

            // 商户订单号，商户网站订单系统中唯一订单号，必填
            String out_trade_no = orderNo;
            // 订单名称，必填
            String subject = orderNo;
            // 付款金额，必填
            String total_amount= totalAmount;
            // 商品描述，可空
            String body = "商品描述";
            // 超时时间 可空
            String timeout_express="2m";
            // 销售产品码 必填
            String product_code="QUICK_WAP_WAY";
            /**********************/
            // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
            //调用RSA签名方式
            AlipayClient client = new DefaultAlipayClient(URL, APPID, RSA_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY,SIGNTYPE);
            AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();

            // 封装请求支付信息
            AlipayTradeWapPayModel other=new AlipayTradeWapPayModel();
            other.setOutTradeNo(out_trade_no);
            other.setSubject(subject);
            other.setTotalAmount(total_amount);
            other.setBody(body);
            other.setTimeoutExpress(timeout_express);
            other.setProductCode(product_code);
            alipay_request.setBizModel(other);
            // 设置异步通知地址
//            alipay_request.setNotifyUrl(AlipayConfig.notify_url);
            // 设置同步地址
//            alipay_request.setReturnUrl(AlipayConfig.return_url);

            // form表单生产
            String form = "";
            try {
                // 调用SDK生成表单
                form = client.pageExecute(alipay_request).getBody();
                response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
                response.getWriter().write(form);//直接将完整的表单html输出到页面
                response.getWriter().flush();
                response.getWriter().close();
            } catch (AlipayApiException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //退款
    public void refund (String orderNo,String totalAmount, HttpServletRequest request, HttpServletResponse response){
        if(request.getParameter("WIDout_trade_no")!=null||request.getParameter("WIDtrade_no")!=null){

            // 商户订单号，商户网站订单系统中唯一订单号，必填
            String out_trade_no = orderNo;
            // 付款金额，必填
            String refund_amount= totalAmount;
            //退款的原因说明
            String refund_reason = new String("退款原因");
            //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
            String  out_request_no = new String(getStringRandom(32));

            // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
            //调用RSA签名方式
            AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
            AlipayTradeRefundRequest alipay_request = new AlipayTradeRefundRequest();

            AlipayTradeRefundModel other=new AlipayTradeRefundModel();
            other.setOutTradeNo(out_trade_no);
            other.setRefundAmount(refund_amount);
            other.setRefundReason(refund_reason);
            other.setOutRequestNo(out_request_no);
            alipay_request.setBizModel(other);

            AlipayTradeRefundResponse alipay_response = null;
            try {
                alipay_response = client.execute(alipay_request);
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
            System.out.println(alipay_response.getBody());
        }
    }

}
