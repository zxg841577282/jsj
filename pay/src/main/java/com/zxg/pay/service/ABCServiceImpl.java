package com.zxg.pay.service;

import com.abc.pay.client.Constants;
import com.abc.pay.client.JSON;
import com.abc.pay.client.ebus.*;
import com.alibaba.fastjson.JSONObject;
import com.zxg.pay.util.XmlJsonMapUtil;
import com.zxg.pay.web.req.abc.*;
import org.springframework.stereotype.Service;
import util.DateUtil;
import util.ListUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/22
 * @Purpose: 农业银行实现类
 */
@Service
public class ABCServiceImpl implements ABCService {

    //支付
    @Override
    public JSONObject payByABC(ABCPayIM im){

        DicOrder dicOrderIm = im.getDicOrderIm();
        List<Orderitem> orderitemIm = im.getOrderitemIm();
        DicRequest dicRequest = im.getDicRequest();
        String SplitMerchantID = im.getSplitMerchantID();
        String SplitAmount = im.getSplitAmount();

        PaymentRequest tPaymentRequest = new PaymentRequest();
        tPaymentRequest.dicOrder.put("PayTypeID", dicOrderIm.getPayTypeID());                   //设定交易类型
        SimpleDateFormat OD = new SimpleDateFormat("yyyy/MM/dd");
        tPaymentRequest.dicOrder.put("OrderDate", OD.format(new Date()));                                       //设定订单日期 （必要信息 - YYYY/MM/DD）
        SimpleDateFormat OT = new SimpleDateFormat("HH:mm:ss");
        tPaymentRequest.dicOrder.put("OrderTime", OT.format(new Date()));                                       //设定订单时间 （必要信息 - HH:MM:SS）
        tPaymentRequest.dicOrder.put("orderTimeoutDate", dicOrderIm.getOrderTimeoutDate());     //设定订单有效期
        tPaymentRequest.dicOrder.put("OrderNo", dicOrderIm.getOrderNo());                       //设定订单编号 （必要信息）
        tPaymentRequest.dicOrder.put("CurrencyCode", dicOrderIm.getCurrencyCode());             //设定交易币种
        tPaymentRequest.dicOrder.put("OrderAmount", dicOrderIm.getOrderAmount());               //设定交易金额
        tPaymentRequest.dicOrder.put("Fee", dicOrderIm.getFee());                               //设定手续费金额
        tPaymentRequest.dicOrder.put("AccountNo", dicOrderIm.getAccountNo());                   //设定支付账户
        tPaymentRequest.dicOrder.put("OrderDesc", dicOrderIm.getOrderDesc());                   //设定订单说明
        tPaymentRequest.dicOrder.put("OrderURL", dicOrderIm.getOrderURL());                     //设定订单地址
        tPaymentRequest.dicOrder.put("ReceiverAddress", dicOrderIm.getReceiverAddress());       //收货地址
        tPaymentRequest.dicOrder.put("InstallmentMark", dicOrderIm.getInstallmentMark());       //分期标识
        if (dicOrderIm.getInstallmentMark().equals("1") && dicOrderIm.getPayTypeID().equals("DividedPay")) {
            tPaymentRequest.dicOrder.put("InstallmentCode", dicOrderIm.getInstallmentCode());   //设定分期代码
            tPaymentRequest.dicOrder.put("InstallmentNum", dicOrderIm.getInstallmentNum());     //设定分期期数
        }
        tPaymentRequest.dicOrder.put("CommodityType", dicOrderIm.getCommodityType());           //设置商品种类
        tPaymentRequest.dicOrder.put("BuyIP", dicOrderIm.getBuyIP());                           //IP
        tPaymentRequest.dicOrder.put("ExpiredDate", dicOrderIm.getExpiredDate());               //设定订单保存时间

        //2、订单明细
        if (ListUtil.ListIsNull(orderitemIm)){
            for (int i = 0; i < orderitemIm.size(); i++) {
                Orderitem oi = orderitemIm.get(i);
                LinkedHashMap orderitem = new LinkedHashMap();
                orderitem.put("SubMerName", oi.getSubMerName());    //设定二级商户名称
                orderitem.put("SubMerId", oi.getSubMerId());    //设定二级商户代码
                orderitem.put("SubMerMCC", oi.getSubMerMCC());   //设定二级商户MCC码
                orderitem.put("SubMerchantRemarks", oi.getSubMerchantRemarks());   //二级商户备注项
                orderitem.put("ProductID", oi.getProductID());//商品代码，预留字段
                orderitem.put("ProductName", oi.getProductName());//商品名称
                orderitem.put("UnitPrice", oi.getUnitPrice());//商品总价
                orderitem.put("Qty", oi.getQty());//商品数量
                orderitem.put("ProductRemarks", oi.getProductRemarks()); //商品备注项
                orderitem.put("ProductType", oi.getProductType());//商品类型
                orderitem.put("ProductDiscount", oi.getProductDiscount());//商品折扣
                orderitem.put("ProductExpiredDate", oi.getProductExpiredDate());//商品有效期
                tPaymentRequest.orderitems.put(i+1, orderitem);
            }
        }

        //3、生成支付请求对象
        String paymentType = dicRequest.getPaymentType();
        tPaymentRequest.dicRequest.put("PaymentType", paymentType);                             //设定支付类型
        String paymentLinkType  = dicRequest.getPaymentLinkType();
        tPaymentRequest.dicRequest.put("PaymentLinkType", paymentLinkType);                     //设定支付接入方式
        if (paymentType.equals(Constants.PAY_TYPE_UCBP) && paymentLinkType.equals(Constants.PAY_LINK_TYPE_MOBILE)) {
            tPaymentRequest.dicRequest.put("UnionPayLinkType",dicRequest.getUnionPayLinkType());  //当支付类型为6，支付接入方式为2的条件满足时，需要设置银联跨行移动支付接入方式
        }
        tPaymentRequest.dicRequest.put("ReceiveAccount", dicRequest.getReceiveAccount());       //设定收款方账号
        tPaymentRequest.dicRequest.put("ReceiveAccName", dicRequest.getReceiveAccName());       //设定收款方户名
        tPaymentRequest.dicRequest.put("NotifyType", dicRequest.getNotifyType());               //设定通知方式
        tPaymentRequest.dicRequest.put("ResultNotifyURL", dicRequest.getResultNotifyURL());     //设定通知URL地址
        tPaymentRequest.dicRequest.put("MerchantRemarks", dicRequest.getMerchantRemarks());     //设定附言
        tPaymentRequest.dicRequest.put("ReceiveMark",dicRequest.getReceiveMark());              //交易是否直接入二级商户账户
        tPaymentRequest.dicRequest.put("ReceiveMerchantType",dicRequest.getReceiveMerchantType()); //设定收款方账户类型
        tPaymentRequest.dicRequest.put("IsBreakAccount", dicRequest.getIsBreakAccount());       //设定交易是否分账、交易是否支持向二级商户入账
        tPaymentRequest.dicRequest.put("SplitAccTemplate", dicRequest.getSplitAccTemplate());   //分账模版编号

        //4、添加分账信息
        String[] SubMerchantID_arr = new String[]{};
        String[] SplitAmount_arr = new String[]{};

        if (SplitMerchantID != null && !SplitMerchantID.equals("")){
            SubMerchantID_arr = SplitMerchantID.split(",");
            SplitAmount_arr = SplitAmount.split(",");
        }

        LinkedHashMap map = null;

        if(SubMerchantID_arr.length > 0){
            for (int i = 0; i < SubMerchantID_arr.length; i++) {
                map = new LinkedHashMap();
                map.put("SplitMerchantID",SubMerchantID_arr[i]);
                map.put("SplitAmount",SplitAmount_arr[i]);

                tPaymentRequest.dicSplitAccInfo.put(i+1, map);
            }
        }

        JSON json = tPaymentRequest.postRequest();
        //JSON json = tPaymentRequest.extendPostRequest(1);

        Map<String,Object> backMap = new HashMap<>();

        String ReturnCode = json.GetKeyValue("ReturnCode");
        String ErrorMessage = json.GetKeyValue("ErrorMessage");
        String OneQRForAll = json.GetKeyValue("OneQRForAll");
        if (ReturnCode.equals("0000")) {
            System.out.println("ReturnCode   = [" + ReturnCode + "]<br/>");
            System.out.println("ErrorMessage = [" + ErrorMessage + "]<br/>");
            System.out.println("PaymentURL-->"+json.GetKeyValue("PaymentURL"));
            System.out.println("OneQRForAll = [" + OneQRForAll + "]<br/>");

            backMap.put("PaymentURL",json.GetKeyValue("PaymentURL"));
            backMap.put("OneQRForAll",OneQRForAll);
            backMap.put("state",true);
            backMap.put("ReturnCode",ReturnCode);
            //response.sendRedirect(json.GetKeyValue("PaymentURL"));
        }
        else {
            System.out.println("---------------支付出现异常Code:"+ ReturnCode +"---------------");
            System.out.println("---------------支付出现异常信息:"+ ErrorMessage +"---------------");
            backMap.put("state",false);
            backMap.put("ReturnCode",ReturnCode);
            backMap.put("ErrorMessage",ErrorMessage);
        }

        return XmlJsonMapUtil.getJSONFromMap(backMap);
    }

    //单笔退款
    @Override
    public JSONObject refundSingle(RefundSingleIM im){

        RefundSingle refundSingle = im.getRefundSingle();
        String SplitMerchantID = im.getSplitMerchantID();
        String SplitAmount = im.getSplitAmount();

        //1、生成退款请求对象
        RefundRequest tRequest = new RefundRequest();
        SimpleDateFormat OD = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat OT = new SimpleDateFormat("HH:mm:ss");
        tRequest.dicRequest.put("OrderDate", OD.format(new Date()));  //订单日期（必要信息）
        tRequest.dicRequest.put("OrderTime", OT.format(new Date())); //订单时间（必要信息）
        tRequest.dicRequest.put("MerRefundAccountNo", refundSingle.getMerRefundAccountNo());  //商户退款账号
        tRequest.dicRequest.put("MerRefundAccountName", refundSingle.getMerRefundAccountName()); //商户退款名
        tRequest.dicRequest.put("OrderNo", refundSingle.getOrderNo()); //原交易编号（必要信息）
        tRequest.dicRequest.put("NewOrderNo", refundSingle.getNewOrderNo()); //交易编号（必要信息）
        tRequest.dicRequest.put("CurrencyCode", refundSingle.getCurrencyCode()); //交易币种（必要信息）
        tRequest.dicRequest.put("TrxAmount", refundSingle.getTrxAmount()); //退货金额 （必要信息）
        tRequest.dicRequest.put("RefundType", refundSingle.getRefundType()); //退货类型 （非必要信息）
        tRequest.dicRequest.put("MerchantRemarks", refundSingle.getMerchantRemarks());  //附言
        tRequest.dicRequest.put("MerRefundAccountFlag", refundSingle.getMerRefundAccountFlag());  //退款账簿上送标识 1：担保账户 2：商户二级账簿 3：退款账簿
        //如果需要专线地址，调用此方法：
        //tRequest.setConnectionFlag(true);

        //4、添加分账信息
        String[] SubMerchantID_arr = new String[]{};
        String[] SplitAmount_arr = new String[]{};

        if (SplitMerchantID != null && !SplitMerchantID.equals("")){
            SubMerchantID_arr = SplitMerchantID.split(",");
            SplitAmount_arr = SplitAmount.split(",");
        }

        LinkedHashMap map = null;

        if(SubMerchantID_arr.length > 0){
            for (int i = 0; i < SubMerchantID_arr.length; i++) {
                map = new LinkedHashMap();
                map.put("SplitMerchantID",SubMerchantID_arr[i]);
                map.put("SplitAmount",SplitAmount_arr[i]);

                tRequest.dicSplitAccInfo.put(i+1, map);
            }
        }

        //3、传送退款请求并取得退货结果
        JSON json = tRequest.postRequest();

        Map<String,Object> backMap = new HashMap<>();

        //4、判断退款结果状态，进行后续操作
        String ReturnCode = json.GetKeyValue("ReturnCode");
        String ErrorMessage = json.GetKeyValue("ErrorMessage");
        if (ReturnCode.equals("0000")) {
            //5、退款成功/退款受理成功
            System.out.println("ReturnCode   = [" + ReturnCode + "]<br/>");
            System.out.println("ErrorMessage = [" + ErrorMessage + "]<br/>");
            System.out.println("OrderNo   = [" + json.GetKeyValue("OrderNo") + "]<br/>");//订单号
            System.out.println("NewOrderNo   = [" + json.GetKeyValue("NewOrderNo") + "]<br/>");//退款交易编号
            System.out.println("TrxAmount = [" + json.GetKeyValue("TrxAmount") + "]<br/>");//退款金额
            System.out.println("BatchNo   = [" + json.GetKeyValue("BatchNo") + "]<br/>");//交易批次号
            System.out.println("VoucherNo = [" + json.GetKeyValue("VoucherNo") + "]<br/>");//交易凭证号
            System.out.println("HostDate  = [" + json.GetKeyValue("HostDate") + "]<br/>");//银行交易日期（YYYY/MM/DD）
            System.out.println("HostTime  = [" + json.GetKeyValue("HostTime") + "]<br/>");//银行交易时间（HH:MM:SS）
            System.out.println("iRspRef  = [" + json.GetKeyValue("iRspRef") + "]<br/>");//银行返回交易流水号
            backMap.put("state",true);
            backMap.put("ReturnCode",ReturnCode);
            backMap.put("ErrorMessage",ErrorMessage);//“交易成功”还是“受理成功”
        }
        else {
            //6、退款失败
            System.out.println("ReturnCode   = [" + ReturnCode + "]<br>");
            System.out.println("ErrorMessage = [" + ErrorMessage + "]<br>");
            backMap.put("state",false);
            backMap.put("ReturnCode",ReturnCode);
            backMap.put("ErrorMessage",ErrorMessage);
        }

        return XmlJsonMapUtil.getJSONFromMap(backMap);
    }
    //批量退款
    private Map<String,Object> refundBatch(List<RefundSingle> list,String MerRefundAccountNo,String MerRefundAccountName){
        //1、生成批量退款请求对象
        BatchRefundRequest tBatchRefundRequest = new BatchRefundRequest();
        //取得明细项
        LinkedHashMap map = null;
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < list.size(); i++) {
            map = new LinkedHashMap();
            RefundSingle refundSingle = list.get(i);
            map.put("SeqNo", String.valueOf(i + 1));
            map.put("OrderNo",refundSingle.getOrderNo());//支付订单编号
            map.put("NewOrderNo",refundSingle.getNewOrderNo());//退款交易编号
            map.put("CurrencyCode",refundSingle.getCurrencyCode());//币种
            map.put("RefundAmount",refundSingle.getTrxAmount());//退款金额
            map.put("Remark",refundSingle.getMerchantRemarks());//附言
            tBatchRefundRequest.dic.put(i+1, map);
            //此处必须使用BigDecimal，否则会丢精度
            BigDecimal bd = new BigDecimal(refundSingle.getTrxAmount());
            sum = sum.add(bd);
        }
        //此处必须设定iSumAmount属性
        tBatchRefundRequest.iSumAmount = sum.doubleValue();

        tBatchRefundRequest.batchRefundRequest.put("BatchNo", ""); //批量编号  （必要信息）
        tBatchRefundRequest.batchRefundRequest.put("BatchDate", DateUtil.getDate(new Date()));  //订单日期  （必要信息）
        tBatchRefundRequest.batchRefundRequest.put("BatchTime",DateUtil.getTime(new Date())); //订单时间  （必要信息）
        tBatchRefundRequest.batchRefundRequest.put("MerRefundAccountNo",MerRefundAccountNo==null?"":MerRefundAccountNo);  //商户退款账号
        tBatchRefundRequest.batchRefundRequest.put("MerRefundAccountName",MerRefundAccountName==null?"":MerRefundAccountName); //商户退款名
        tBatchRefundRequest.batchRefundRequest.put("TotalCount",list.size());  //总笔数  （必要信息）
        tBatchRefundRequest.batchRefundRequest.put("TotalAmount",sum);  //总金额 （必要信息）

        //2、传送批量退款请求并取得结果
        JSON json = tBatchRefundRequest.postRequest();

        Map<String,Object> backMap = new HashMap<>();

        //3、判断批量退款结果状态，进行后续操作
        String ReturnCode = json.GetKeyValue("ReturnCode");
        String ErrorMessage = json.GetKeyValue("ErrorMessage");
        if (ReturnCode.equals("0000")) {
            //4、批量退款成功
            System.out.println("ReturnCode   = [" + ReturnCode + "]<br/>");
            System.out.println("ResultMessage   = [" + ErrorMessage + "]<br/>");
            System.out.println("TrxType   = [" + json.GetKeyValue("TrxType") + "]<br/>");//交易种类
            System.out.println("TotalCount  = [" + json.GetKeyValue("TotalCount") + "]<br/>");//交易总笔数
            System.out.println("TotalAmount = [" + json.GetKeyValue("TotalAmount") + "]<br/>");//交易总金额
            System.out.println("SerialNumber  = [" + json.GetKeyValue("SerialNumber") + "]<br/>");//批量编号
            System.out.println("HostDate  = [" + json.GetKeyValue("HostDate") + "]<br/>");
            System.out.println("HostTime  = [" + json.GetKeyValue("HostTime") + "]<br/>");
            backMap.put("state",true);
            backMap.put("ReturnCode",ReturnCode);
            backMap.put("ErrorMessage",ErrorMessage);//“交易成功”还是“受理成功”
            backMap.put("SerialNumber",json.GetKeyValue("SerialNumber"));//批量编号
        }
        else {
            //5、批量退款失败
            System.out.println("ReturnCode   = [" + ReturnCode + "]<br/>");
            System.out.println("ResultMessage   = [" + ErrorMessage + "]<br/>");
            backMap.put("state",false);
            backMap.put("ReturnCode",ReturnCode);
            backMap.put("ErrorMessage",ErrorMessage);
        }

        return backMap;
    }

    //批量退款查询
    private Map<String,Object> getRefundInfoBatch(String SerialNumber){
        //1、生成退款批量结果查询请求对象
        QueryBatchRequest tQueryBatchRequest = new QueryBatchRequest();
        SimpleDateFormat OD = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat OT = new SimpleDateFormat("HH:mm:ss");
        tQueryBatchRequest.queryBatchRequest.put("BatchDate",OD.format(new Date()));     //订单日期（必要信息）
        tQueryBatchRequest.queryBatchRequest.put("BatchTime",OT.format(new Date()));     //订单时间（必要信息）
        tQueryBatchRequest.queryBatchRequest.put("SerialNumber",SerialNumber); //设定退款批量结果查询请求的流水号（必要信息）

        //2、传送退款批量结果查询请求并取得结果
        JSON json = tQueryBatchRequest.postRequest();

        Map<String,Object> backMap = new HashMap<>();

        //3、判断退款批量结果查询状态，进行后续操作
        String ReturnCode = json.GetKeyValue("ReturnCode");
        String ErrorMessage = json.GetKeyValue("ErrorMessage");
        if (ReturnCode.equals("0000")) {
            //4、生成批量对象
            System.out.println("ReturnCode      = [" + ReturnCode + "]<br/>");
            System.out.println("ErrorMessage      = [" + ErrorMessage + "]<br/>");
            System.out.println("BatchDate      = [" + json.GetKeyValue("BatchDate").toString() + "]<br/>");
            System.out.println("BatchTime  = [" + json.GetKeyValue("BatchTime").toString() + "]<br/>");
            System.out.println("SerialNumber  = [" + json.GetKeyValue("SerialNumber").toString() + "]<br/>");
            System.out.println("BatchStatus  = [" + json.GetKeyValue("BatchStatus").toString() + "]<br/>");
            System.out.println("MerRefundAccountNo  = [" + json.GetKeyValue("MerRefundAccountNo").toString() + "]<br/>");
            System.out.println("MerRefundAccountName  = [" + json.GetKeyValue("MerRefundAccountName").toString() + "]<br/>");
            System.out.println("RefundAmount  = [" + json.GetKeyValue("RefundAmount").toString() + "]<br/>");
            System.out.println("RefundCount    = [" + json.GetKeyValue("RefundCount").toString() + "]<br/>");
            System.out.println("SuccessAmount      = [" + json.GetKeyValue("SuccessAmount").toString() + "]<br/>");
            System.out.println("SuccessCount      = [" + json.GetKeyValue("SuccessCount").toString() + "]<br/>");
            System.out.println("FailedAmount      = [" + json.GetKeyValue("FailedAmount").toString() + "]<br/>");
            System.out.println("FailedCount      = [" + json.GetKeyValue("FailedCount").toString() + "]<br/>");
            backMap.put("state",true);
            backMap.put("ReturnCode",ReturnCode);
            backMap.put("ErrorMessage",ErrorMessage);

            //5、取得订单明细
            LinkedHashMap tOrders = json.GetArrayValue("Order");
            if (tOrders.size() <= 0) {
                System.out.println("明细为空！<br/>");
                backMap.put("body","明细为空！");
            } else {
                backMap.put("body",tOrders);
                Iterator iter = tOrders.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Hashtable val = (Hashtable)entry.getValue();
                    System.out.println("OriginalOrderNo      = [" + (String)val.get("OriginalOrderNo") + "],");
                    System.out.println("RefundOrderNo      = [" + (String)val.get("RefundOrderNo") + "],");
                    System.out.println("CurrencyCode      = [" + (String)val.get("CurrencyCode") + "],");
                    System.out.println("RefundAmountCell      = [" + (String)val.get("RefundAmountCell") + "],");
                    System.out.println("OrderStatus      = [" + (String)val.get("OrderStatus") + "],");
                    System.out.println("Remark      = [" + (String)val.get("Remark") + "],");
                }
            }
        } else {
            //6、退款批量结果查询失败
            System.out.println("ReturnCode   = [" + ReturnCode + "]<br/>");
            System.out.println("ErrorMessage = [" + ErrorMessage + "]<br/>");
            backMap.put("state",false);
            backMap.put("ReturnCode",ReturnCode);
            backMap.put("ErrorMessage",ErrorMessage);
        }

        return backMap;
    }

    //单笔查询
    @Override
    public JSONObject getABCOrder(GetInfoSingle getInfoSingle){
        String payTypeID = getInfoSingle.getPayTypeID();
        String queryTpye = getInfoSingle.getQueryDetail();

        QueryOrderRequest tQueryRequest = new QueryOrderRequest();
        tQueryRequest.queryRequest.put("PayTypeID", payTypeID);    //设定交易类型
        tQueryRequest.queryRequest.put("OrderNo", getInfoSingle.getOrderNo());    //设定订单编号 （必要信息）
        tQueryRequest.queryRequest.put("QueryDetail", queryTpye);//设定查询方式
        //如果需要专线地址，调用此方法：
        //tQueryRequest.setConnectionFlag(true);
        JSON json = tQueryRequest.postRequest();
        //JSON json = tQueryRequest.extendPostRequest(1);

        Map<String,Object> backMap = new HashMap<>();

        String ReturnCode = json.GetKeyValue("ReturnCode");
        String ErrorMessage = json.GetKeyValue("ErrorMessage");

        if (ReturnCode.equals("0000")) {
            System.out.println("ReturnCode   = [" + ReturnCode + "]<br/>");
            System.out.println("ErrorMessage = [" + ErrorMessage + "]<br/>");
            backMap.put("state",true);
            backMap.put("ReturnCode",ReturnCode);
            backMap.put("ErrorMessage",ErrorMessage);
            //4、获取结果信息
            String orderInfo = json.GetKeyValue("Order");
            if (orderInfo.length() < 1) {
                System.out.println("查询结果为空<br/>");
                backMap.put("body","查询结果为空");
            } else {
                //1、还原经过base64编码的信息
                com.abc.pay.client.Base64 tBase64 = new com.abc.pay.client.Base64();
                String orderDetail = new String(tBase64.decode(orderInfo));
                json.setJsonString(orderDetail);
                System.out.println("订单明细" + orderDetail + "<br/>");

                if(queryTpye.equals("0")) {     //状态查询
                    System.out.println("PayTypeID      = [" + json.GetKeyValue("PayTypeID") + "]<br/>");
                    System.out.println("OrderNo      = [" + json.GetKeyValue("OrderNo") + "]<br/>");
                    System.out.println("OrderDate      = [" + json.GetKeyValue("OrderDate") + "]<br/>");
                    System.out.println("OrderTime      = [" + json.GetKeyValue("OrderTime") + "]<br/>");
                    System.out.println("OrderAmount      = [" + json.GetKeyValue("OrderAmount") + "]<br/>");
                    System.out.println("Status      = [" + json.GetKeyValue("Status") + "]<br/>");
                    backMap.put("状态",getStatus(json.GetKeyValue("Status")));
                } else {        //详细查询
                    backMap.put("订单明细",orderDetail);
                    LinkedHashMap hashMap = new LinkedHashMap();
                    //直接支付或预授权支付
                    if (payTypeID.equals("ImmediatePay") || payTypeID.equals("PreAuthPay")) {
                        System.out.println("PayTypeID      = [" + json.GetKeyValue("PayTypeID") + "]<br/>");
                        System.out.println("OrderNo      = [" + json.GetKeyValue("OrderNo") + "]<br/>");
                        System.out.println("OrderDate      = [" + json.GetKeyValue("OrderDate") + "]<br/>");
                        System.out.println("OrderTime      = [" + json.GetKeyValue("OrderTime") + "]<br/>");
                        System.out.println("OrderAmount      = [" + json.GetKeyValue("OrderAmount") + "]<br/>");
                        System.out.println("Status      = [" + json.GetKeyValue("Status") + "]<br/>");
                        System.out.println("OrderDesc      = [" + json.GetKeyValue("OrderDesc") + "]<br/>");
                        System.out.println("OrderURL      = [" + json.GetKeyValue("OrderURL") + "]<br/>");
                        System.out.println("PaymentLinkType      = [" + json.GetKeyValue("PaymentLinkType") + "]<br/>");
                        System.out.println("AcctNo      = [" + json.GetKeyValue("AcctNo") + "]<br/>");
                        System.out.println("CommodityType      = [" + json.GetKeyValue("CommodityType") + "]<br/>");
                        System.out.println("ReceiverAddress      = [" + json.GetKeyValue("ReceiverAddress") + "]<br/>");
                        System.out.println("BuyIP      = [" + json.GetKeyValue("BuyIP") + "]<br/>");
                        System.out.println("iRspRef      = [" + json.GetKeyValue("iRspRef") + "]<br/>");
                        //out.println("HostTime      = [" + json.GetKeyValue("HostTime") + "]<br/>");
                        //out.println("HostDate      = [" + json.GetKeyValue("HostDate") + "]<br/>");
                        System.out.println("ReceiveAccount      = [" + json.GetKeyValue("ReceiveAccount") + "]<br/>");
                        System.out.println("ReceiveAccName      = [" + json.GetKeyValue("ReceiveAccName") + "]<br/>");
                        System.out.println("MerchantRemarks      = [" + json.GetKeyValue("MerchantRemarks") + "]<br/>");

                        //5、商品明细
                        hashMap = json.GetArrayValue("OrderItems");
                        if(hashMap.size() == 0){
                            System.out.println("商品明细为空");
                        } else {
                            System.out.println("商品明细为:<br/>");
                            Iterator iter = hashMap.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                //Object key = entry.getKey();
                                Hashtable val = (Hashtable)entry.getValue();

                                System.out.println("SubMerName      = [" + (String)val.get("SubMerName") + "],");
                                System.out.println("SubMerId      = [" + (String)val.get("SubMerId") + "],");
                                System.out.println("SubMerMCC      = [" + (String)val.get("SubMerMCC") + "],");
                                System.out.println("SubMerchantRemarks      = [" + (String)val.get("SubMerchantRemarks") + "],");
                                System.out.println("ProductID      = [" + (String)val.get("ProductID") + "],");
                                System.out.println("ProductName      = [" + (String)val.get("ProductName") + "],");
                                System.out.println("UnitPrice      = [" + (String)val.get("UnitPrice") + "],");
                                System.out.println("Qty      = [" + (String)val.get("Qty") + "],");
                                System.out.println("ProductRemarks      = [" + (String)val.get("ProductRemarks") + "],");
                            }
                        }
                    } else if (payTypeID.equals("DividedPay")) {
                        System.out.println("PayTypeID      = [" + json.GetKeyValue("PayTypeID") + "]<br/>");
                        System.out.println("OrderNo      = [" + json.GetKeyValue("OrderNo") + "]<br/>");
                        System.out.println("OrderDate      = [" + json.GetKeyValue("OrderDate") + "]<br/>");
                        System.out.println("OrderTime      = [" + json.GetKeyValue("OrderTime") + "]<br/>");
                        System.out.println("OrderAmount      = [" + json.GetKeyValue("OrderAmount") + "]<br/>");
                        System.out.println("Status      = [" + json.GetKeyValue("Status") + "]<br/>");
                        System.out.println("InstallmentCode      = [" + json.GetKeyValue("InstallmentCode") + "]<br/>");
                        System.out.println("InstallmentNum      = [" + json.GetKeyValue("InstallmentNum") + "]<br/>");
                        System.out.println("PaymentLinkType      = [" + json.GetKeyValue("PaymentLinkType") + "]<br/>");
                        System.out.println("AcctNo      = [" + json.GetKeyValue("AcctNo") + "]<br/>");
                        System.out.println("CommodityType      = [" + json.GetKeyValue("CommodityType") + "]<br/>");
                        System.out.println("ReceiverAddress      = [" + json.GetKeyValue("ReceiverAddress") + "]<br/>");
                        System.out.println("BuyIP      = [" + json.GetKeyValue("BuyIP") + "]<br/>");
                        System.out.println("iRspRef      = [" + json.GetKeyValue("iRspRef") + "]<br/>");
                        //out.println("HostTime      = [" + json.GetKeyValue("HostTime") + "]<br/>");
                        //out.println("HostDate      = [" + json.GetKeyValue("HostDate") + "]<br/>");
                        System.out.println("ReceiveAccount      = [" + json.GetKeyValue("ReceiveAccount") + "]<br/>");
                        System.out.println("ReceiveAccName      = [" + json.GetKeyValue("ReceiveAccName") + "]<br/>");
                        System.out.println("MerchantRemarks      = [" + json.GetKeyValue("MerchantRemarks") + "]<br/>");

                        hashMap = json.GetArrayValue("OrderItems");
                        if(hashMap.size() == 0){
                            System.out.println("商品明细为空");
                        } else {
                            System.out.println("商品明细为:<br/>");
                            Iterator iter = hashMap.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                //Object key = entry.getKey();
                                Hashtable val = (Hashtable)entry.getValue();
                                System.out.println("SubMerName      = [" + (String)val.get("SubMerName") + "],");
                                System.out.println("SubMerId      = [" + (String)val.get("SubMerId") + "],");
                                System.out.println("SubMerMCC      = [" + (String)val.get("SubMerMCC") + "],");
                                System.out.println("SubMerchantRemarks      = [" + (String)val.get("SubMerchantRemarks") + "],");
                                System.out.println("ProductID      = [" + (String)val.get("ProductID") + "],");
                                System.out.println("ProductName      = [" + (String)val.get("ProductName") + "],");
                                System.out.println("UnitPrice      = [" + (String)val.get("UnitPrice") + "],");
                                System.out.println("Qty      = [" + (String)val.get("Qty") + "],");
                                System.out.println("ProductRemarks      = [" + (String)val.get("ProductRemarks") + "],");
                            }
                        }
                        hashMap.clear();
                        hashMap = json.GetArrayValue("Distribution");
                        if (hashMap.size() == 0) {
                            System.out.println("分账账户信息为空");
                        } else {
                            System.out.println("分账账户信息明细为:<br/>");
                            Iterator iter = hashMap.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                //Object key = entry.getKey();
                                Hashtable val = (Hashtable)entry.getValue();
                                System.out.println("DisAccountNo      = [" + (String)val.get("DisAccountNo") + "],");
                                System.out.println("DisAccountName      = [" + (String)val.get("DisAccountName") + "],");
                                System.out.println("DisAmount      = [" + (String)val.get("DisAmount") + "],");
                            }
                        }
                    } else if (payTypeID.equals("Refund")) {
                        System.out.println("PayTypeID      = [" + json.GetKeyValue("PayTypeID") + "]<br/>");
                        System.out.println("OrderNo      = [" + json.GetKeyValue("OrderNo") + "]<br/>");
                        System.out.println("OrderDate      = [" + json.GetKeyValue("OrderDate") + "]<br/>");
                        System.out.println("OrderTime      = [" + json.GetKeyValue("OrderTime") + "]<br/>");
                        System.out.println("RefundAmount      = [" + json.GetKeyValue("RefundAmount") + "]<br/>");
                        System.out.println("Status      = [" + json.GetKeyValue("Status") + "]<br/>");
                        System.out.println("iRspRef      = [" + json.GetKeyValue("iRspRef") + "]<br/>");
                        //out.println("HostTime      = [" + json.GetKeyValue("HostTime") + "]<br/>");
                        //out.println("HostDate      = [" + json.GetKeyValue("HostDate") + "]<br/>");
                        System.out.println("MerRefundAccountNo      = [" + json.GetKeyValue(" MerRefundAccountNo") + "]<br/>");
                        System.out.println("MerRefundAccountName      = [" + json.GetKeyValue(" MerRefundAccountName") + "]<br/>");
                    } else if (payTypeID.equals("AgentPay")) {
                        System.out.println("PayTypeID      = [" + json.GetKeyValue("PayTypeID") + "]<br/>");
                        System.out.println("OrderNo      = [" + json.GetKeyValue("OrderNo") + "]<br/>");
                        System.out.println("OrderDate      = [" + json.GetKeyValue("OrderDate") + "]<br/>");
                        System.out.println("OrderTime      = [" + json.GetKeyValue("OrderTime") + "]<br/>");
                        System.out.println("OrderAmount      = [" + json.GetKeyValue("OrderAmount") + "]<br/>");
                        System.out.println("Status      = [" + json.GetKeyValue("Status") + "]<br/>");
                        System.out.println("InstallmentCode      = [" + json.GetKeyValue("InstallmentCode") + "]<br/>");
                        System.out.println("InstallmentNum      = [" + json.GetKeyValue("InstallmentNum") + "]<br/>");
                        System.out.println("PaymentLinkType      = [" + json.GetKeyValue("PaymentLinkType") + "]<br/>");
                        System.out.println("AcctNo      = [" + json.GetKeyValue("AcctNo") + "]<br/>");
                        System.out.println("CommodityType      = [" + json.GetKeyValue("CommodityType") + "]<br/>");
                        System.out.println("ReceiverAddress      = [" + json.GetKeyValue("ReceiverAddress") + "]<br/>");
                        System.out.println("BuyIP      = [" + json.GetKeyValue("BuyIP") + "]<br/>");
                        System.out.println("iRspRef      = [" + json.GetKeyValue("iRspRef") + "]<br/>");
                        //out.println("HostTime      = [" + json.GetKeyValue("HostTime") + "]<br/>");
                        //out.println("HostDate      = [" + json.GetKeyValue("HostDate") + "]<br/>");
                        System.out.println("ReceiveAccount      = [" + json.GetKeyValue("ReceiveAccount") + "]<br/>");
                        System.out.println("ReceiveAccName      = [" + json.GetKeyValue("ReceiveAccName") + "]<br/>");
                        System.out.println("MerchantRemarks      = [" + json.GetKeyValue("MerchantRemarks") + "]<br/>");

                        hashMap = json.GetArrayValue("OrderItem");
                        if(hashMap.size() == 0){
                            System.out.println("商品明细为空");
                        } else {
                            System.out.println("商品明细为:<br/>");
                            Iterator iter = hashMap.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                //Object key = entry.getKey();
                                Hashtable val = (Hashtable)entry.getValue();
                                System.out.println("SubMerName      = [" + (String)val.get("SubMerName") + "],");
                                System.out.println("SubMerId      = [" + (String)val.get("SubMerId") + "],");
                                System.out.println("SubMerMCC      = [" + (String)val.get("SubMerMCC") + "],");
                                System.out.println("SubMerchantRemarks      = [" + (String)val.get("SubMerchantRemarks") + "],");
                                System.out.println("ProductID      = [" + (String)val.get("ProductID") + "],");
                                System.out.println("ProductName      = [" + (String)val.get("ProductName") + "],");
                                System.out.println("UnitPrice      = [" + (String)val.get("UnitPrice") + "],");
                                System.out.println("Qty      = [" + (String)val.get("Qty") + "],");
                                System.out.println("ProductRemarks      = [" + (String)val.get("ProductRemarks") + "],");
                            }
                        }
                        //4、获取分账账户信息
                        hashMap.clear();
                        hashMap = json.GetArrayValue("Distribution");
                        if (hashMap.size() == 0) {
                            System.out.println("分账账户信息为空");
                        } else {
                            System.out.println("分账账户信息明细为:<br/>");
                            Iterator iter = hashMap.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                //Object key = entry.getKey();
                                Hashtable val = (Hashtable)entry.getValue();
                                System.out.println("DisAccountNo      = [" + (String)val.get("DisAccountNo") + "],");
                                System.out.println("DisAccountName      = [" + (String)val.get("DisAccountName") + "],");
                                System.out.println("DisAmount      = [" + (String)val.get("DisAmount") + "],");
                            }
                        }
                    } else if (payTypeID.equals("PreAuthed") || payTypeID.equals("PreAuthCancel")) {
                        System.out.println("PayTypeID      = [" + json.GetKeyValue("PayTypeID") + "]<br/>");
                        System.out.println("OrderNo      = [" + json.GetKeyValue("OrderNo") + "]<br/>");
                        System.out.println("OrderDate      = [" + json.GetKeyValue("OrderDate") + "]<br/>");
                        System.out.println("OrderTime      = [" + json.GetKeyValue("OrderTime") + "]<br/>");
                        System.out.println("OrderAmount      = [" + json.GetKeyValue("OrderAmount") + "]<br/>");
                        System.out.println("Status      = [" + json.GetKeyValue("Status") + "]<br/>");
                        System.out.println("AcctNo      = [" + json.GetKeyValue("AcctNo") + "]<br/>");
                        System.out.println("iRspRef      = [" + json.GetKeyValue("iRspRef") + "]<br/>");
                        //out.println("HostTime      = [" + json.GetKeyValue("HostTime") + "]<br/>");
                        //out.println("HostDate      = [" + json.GetKeyValue("HostDate") + "]<br/>");
                        System.out.println("ReceiveAccount      = [" + json.GetKeyValue("ReceiveAccount") + "]<br/>");
                        System.out.println("ReceiveAccName      = [" + json.GetKeyValue("ReceiveAccName") + "]<br/>");
                    }
                }
            }
        } else {
            //6、商户订单查询失败
            System.out.println("ReturnCode   = [" + ReturnCode + "]<br/>");
            System.out.println("ErrorMessage = [" + ErrorMessage + "]<br/>");
            backMap.put("state",false);
            backMap.put("ReturnCode",ReturnCode);
            backMap.put("ErrorMessage",ErrorMessage);
        }
        return XmlJsonMapUtil.getJSONFromMap(backMap);
    }

    //订单状态信息转换
    private String getStatus(String status){
        String back = "";
        switch (status){
            case "01":back = "未支付";break;
            case "02":back = "无回应";break;
            case "03":back = "微信和支付宝支付成功";break;
            case "04":back = "选择微信、支付宝支付时，表示农行清算成功；选择我行支付通道支付时，表示农行支付成功";break;
            case "05":back = "已退款";break;
            case "07":back = "授权确认成功";break;
            case "00":back = "授权已取消";break;
            case "99":back = "失败";break;
            default:back = "未知状态";System.out.println("此时未知状态为："+ status);
        }
        return back;
    }

    //微信支付
    @Override
    public JSONObject payByWx(ABCWXPayIM im){

        DicOrderByWX dicOrderIm = im.getDicOrderIm();
        List<Orderitem> orderitemIm = im.getOrderitemIm();
        DicRequestWX dicRequest = im.getDicRequest();
        String SplitMerchantID = im.getSplitMerchantID();
        String SplitAmount = im.getSplitAmount();

        UnifiedPaymentRequest tPaymentRequest = new UnifiedPaymentRequest();
        tPaymentRequest.dicOrder.put("PayTypeID", dicOrderIm.getPayTypeID());                   //设定交易类型
        SimpleDateFormat ODSDF = new SimpleDateFormat("yyyy/MM/dd");
        tPaymentRequest.dicOrder.put("OrderDate", ODSDF.format(new Date()));                    //设定订单日期 （必要信息 - YYYY/MM/DD）
        SimpleDateFormat OTSDF = new SimpleDateFormat("HH:mm:ss");
        tPaymentRequest.dicOrder.put("OrderTime", OTSDF.format(new Date()));                    //设定订单时间 （必要信息 - HH:MM:SS）
        tPaymentRequest.dicOrder.put("orderTimeoutDate", dicOrderIm.getOrderTimeoutDate());     //设定订单有效期
        tPaymentRequest.dicOrder.put("OrderNo", dicOrderIm.getOrderNo());                       //设定订单编号 （必要信息）
        tPaymentRequest.dicOrder.put("CurrencyCode", dicOrderIm.getCurrencyCode());             //设定交易币种
        tPaymentRequest.dicOrder.put("OrderAmount", dicOrderIm.getOrderAmount());               //设定交易金额
        tPaymentRequest.dicOrder.put("Fee", dicOrderIm.getFee());                               //设定手续费金额
        tPaymentRequest.dicOrder.put("AccountNo", dicOrderIm.getAccountNo());                   //设定微信商户信息
        tPaymentRequest.dicOrder.put("OpenID", dicOrderIm.getOpenID());                         //设定openID
        tPaymentRequest.dicOrder.put("OrderDesc", dicOrderIm.getOrderDesc());                   //设定订单说明
        tPaymentRequest.dicOrder.put("ReceiverAddress", dicOrderIm.getReceiverAddress());       //收货地址
        tPaymentRequest.dicOrder.put("InstallmentMark", dicOrderIm.getInstallmentMark());       //分期标识
        if (dicOrderIm.getInstallmentMark().equals("1") && dicOrderIm.getPayTypeID().equals("DividedPay")) {
            tPaymentRequest.dicOrder.put("InstallmentCode", dicOrderIm.getInstallmentCode());   //设定分期代码
            tPaymentRequest.dicOrder.put("InstallmentNum", dicOrderIm.getInstallmentNum());     //设定分期期数
        }
        tPaymentRequest.dicOrder.put("BuyIP", dicOrderIm.getBuyIP());                           //IP
        tPaymentRequest.dicOrder.put("ExpiredDate", dicOrderIm.getExpiredDate());               //设定订单保存时间
        tPaymentRequest.dicOrder.put("LimitPay", dicOrderIm.getLimitPay());                     //设定是否支持借贷卡

        //2、订单明细
        if (ListUtil.ListIsNull(orderitemIm)){
            for (int i = 0; i < orderitemIm.size(); i++) {
                Orderitem oi = orderitemIm.get(i);
                LinkedHashMap orderitem = new LinkedHashMap();
                orderitem.put("SubMerName", oi.getSubMerName());    //设定二级商户名称
                orderitem.put("SubMerId", oi.getSubMerId());    //设定二级商户代码
                orderitem.put("SubMerMCC", oi.getSubMerMCC());   //设定二级商户MCC码
                orderitem.put("SubMerchantRemarks", oi.getSubMerchantRemarks());   //二级商户备注项
                orderitem.put("ProductID", oi.getProductID());//商品代码，预留字段
                orderitem.put("ProductName", oi.getProductName());//商品名称
                orderitem.put("UnitPrice", oi.getUnitPrice());//商品总价
                orderitem.put("Qty", oi.getQty());//商品数量
                orderitem.put("ProductRemarks", oi.getProductRemarks()); //商品备注项
                orderitem.put("ProductType", oi.getProductType());//商品类型
                orderitem.put("ProductDiscount", oi.getProductDiscount());//商品折扣
                orderitem.put("ProductExpiredDate", oi.getProductExpiredDate());//商品有效期
                tPaymentRequest.orderitems.put(i+1, orderitem);
            }
        }

        //3、生成支付请求对象
        tPaymentRequest.dicRequest.put("CommodityType", dicRequest.getCommodityType());           //设置商品种类
        tPaymentRequest.dicRequest.put("PaymentType", dicRequest.getPaymentType());                             //设定支付类型
        tPaymentRequest.dicRequest.put("PaymentLinkType", dicRequest.getPaymentLinkType());                     //设定支付接入方式
        tPaymentRequest.dicRequest.put("NotifyType", dicRequest.getNotifyType());               //设定通知方式
        tPaymentRequest.dicRequest.put("ResultNotifyURL", dicRequest.getResultNotifyURL());     //设定通知URL地址
        tPaymentRequest.dicRequest.put("MerchantRemarks", dicRequest.getMerchantRemarks());     //设定附言
        tPaymentRequest.dicRequest.put("IsBreakAccount", dicRequest.getIsBreakAccount());       //设定交易是否分账、交易是否支持向二级商户入账

        //4、添加分账信息
        String[] SubMerchantID_arr = new String[]{};
        String[] SplitAmount_arr = new String[]{};

        if (SplitMerchantID != null && !SplitMerchantID.equals("")){
            SubMerchantID_arr = SplitMerchantID.split(",");
            SplitAmount_arr = SplitAmount.split(",");
        }

        LinkedHashMap map = null;

        if(SubMerchantID_arr.length > 0){
            for (int i = 0; i < SubMerchantID_arr.length; i++) {
                map = new LinkedHashMap();
                map.put("SplitMerchantID",SubMerchantID_arr[i]);
                map.put("SplitAmount",SplitAmount_arr[i]);

                tPaymentRequest.dicSplitAccInfo.put(i+1, map);
            }
        }

        JSON json = tPaymentRequest.postRequest();
        //JSON json = tPaymentRequest.extendPostRequest(1);

        Map<String,Object> backMap = new HashMap<>();

        String ReturnCode = json.GetKeyValue("ReturnCode");
        String ErrorMessage = json.GetKeyValue("ErrorMessage");
        String OneQRForAll = json.GetKeyValue("OneQRForAll");
        String PrePayID = json.GetKeyValue("PrePayID");
        String JSAPI = json.GetKeyValue("JSAPI");
        if (ReturnCode.equals("0000")) {
            System.out.println("ReturnCode   = [" + ReturnCode + "]<br/>");
            System.out.println("ErrorMessage = [" + ErrorMessage + "]<br/>");
            System.out.println("PaymentURL-->"+json.GetKeyValue("PaymentURL"));
            System.out.println("OneQRForAll = [" + OneQRForAll + "]<br/>");
            System.out.println("PrePayID = [" + PrePayID + "]<br/>");
            System.out.println("JSAPI = [" + JSAPI + "]<br/>");

            backMap.put("PaymentURL",json.GetKeyValue("PaymentURL"));
            backMap.put("OneQRForAll",OneQRForAll);
            backMap.put("state",true);
            backMap.put("ReturnCode",ReturnCode);
            backMap.put("PrePayID",PrePayID);
            backMap.put("JSAPI",JSAPI);
            //response.sendRedirect(json.GetKeyValue("PaymentURL"));
        }
        else {
            System.out.println("---------------支付出现异常Code:"+ ReturnCode +"---------------");
            System.out.println("---------------支付出现异常信息:"+ ErrorMessage +"---------------");
            backMap.put("state",false);
            backMap.put("ReturnCode",ReturnCode);
            backMap.put("ErrorMessage",ErrorMessage);
        }

        return XmlJsonMapUtil.getJSONFromMap(backMap);
    }

    //支付宝支付
    public JSONObject payByAli(DicOrderByAli dicOrderIm, List<Orderitem> orderitemIm, DicRequestAli dicRequest, String SplitMerchantID, String SplitAmount){
        AlipayRequest tPaymentRequest = new AlipayRequest();
        tPaymentRequest.dicOrder.put("PayTypeID", dicOrderIm.getPayTypeID());                   //设定交易类型
        tPaymentRequest.dicOrder.put("OrderDate", dicOrderIm.getOrderDate());                   //设定订单日期 （必要信息 - YYYY/MM/DD）
        tPaymentRequest.dicOrder.put("OrderTime", dicOrderIm.getOrderTime());                   //设定订单时间 （必要信息 - HH:MM:SS）
        tPaymentRequest.dicOrder.put("orderTimeoutDate", dicOrderIm.getOrderTimeoutDate());     //设定订单有效期
        tPaymentRequest.dicOrder.put("PAYED_RETURN_URL", dicOrderIm.getPAYED_RETURN_URL());     //设定支付后回调地址
        tPaymentRequest.dicOrder.put("OrderNo", dicOrderIm.getOrderNo());                       //设定订单编号 （必要信息）
        tPaymentRequest.dicOrder.put("CurrencyCode", dicOrderIm.getCurrencyCode());             //设定交易币种
        tPaymentRequest.dicOrder.put("OrderAmount", dicOrderIm.getOrderAmount());               //设定交易金额
        tPaymentRequest.dicOrder.put("Fee", dicOrderIm.getFee());                               //设定手续费金额
        tPaymentRequest.dicOrder.put("AccountNo", dicOrderIm.getAccountNo());                   //设定微信商户信息
        tPaymentRequest.dicOrder.put("OrderDesc", dicOrderIm.getOrderDesc());                   //设定订单说明
        tPaymentRequest.dicOrder.put("ReceiverAddress", dicOrderIm.getReceiverAddress());       //收货地址
        tPaymentRequest.dicOrder.put("InstallmentMark", dicOrderIm.getInstallmentMark());       //分期标识
        if (dicOrderIm.getInstallmentMark().equals("1") && dicOrderIm.getPayTypeID().equals("DividedPay")) {
            tPaymentRequest.dicOrder.put("InstallmentCode", dicOrderIm.getInstallmentCode());   //设定分期代码
            tPaymentRequest.dicOrder.put("InstallmentNum", dicOrderIm.getInstallmentNum());     //设定分期期数
        }
        tPaymentRequest.dicOrder.put("BuyIP", dicOrderIm.getBuyIP());                           //IP
        tPaymentRequest.dicOrder.put("ExpiredDate", dicOrderIm.getExpiredDate());               //设定订单保存时间
        tPaymentRequest.dicOrder.put("WapQuitUrl", dicOrderIm.getWapQuitUrl());                 //设定WAP支付中途退出返回网址
        tPaymentRequest.dicOrder.put("PcQrPayMode", dicOrderIm.getPcQrPayMode());               //设定扫码支付方式
        tPaymentRequest.dicOrder.put("PcQrCodeWidth", dicOrderIm.getPcQrCodeWidth());           //设定自定义二维码宽度
        tPaymentRequest.dicOrder.put("TimeoutExpress", dicOrderIm.getTimeoutExpress());         //设定支付宝订单有效期
        tPaymentRequest.dicOrder.put("ChildMerchantNo", dicOrderIm.getChildMerchantNo());       //设定子商户(大商户模式)

        //2、订单明细
        if (ListUtil.ListIsNull(orderitemIm)){
            for (int i = 0; i < orderitemIm.size(); i++) {
                Orderitem oi = orderitemIm.get(i);
                LinkedHashMap orderitem = new LinkedHashMap();
                orderitem.put("SubMerName", oi.getSubMerName());    //设定二级商户名称
                orderitem.put("SubMerId", oi.getSubMerId());    //设定二级商户代码
                orderitem.put("SubMerMCC", oi.getSubMerMCC());   //设定二级商户MCC码
                orderitem.put("SubMerchantRemarks", oi.getSubMerchantRemarks());   //二级商户备注项
                orderitem.put("ProductID", oi.getProductID());//商品代码，预留字段
                orderitem.put("ProductName", oi.getProductName());//商品名称
                orderitem.put("UnitPrice", oi.getUnitPrice());//商品总价
                orderitem.put("Qty", oi.getQty());//商品数量
                orderitem.put("ProductRemarks", oi.getProductRemarks()); //商品备注项
                orderitem.put("ProductType", oi.getProductType());//商品类型
                orderitem.put("ProductDiscount", oi.getProductDiscount());//商品折扣
                orderitem.put("ProductExpiredDate", oi.getProductExpiredDate());//商品有效期
                tPaymentRequest.orderitems.put(i+1, orderitem);
            }
        }

        //3、生成支付请求对象
        tPaymentRequest.dicRequest.put("CommodityType", dicRequest.getCommodityType());           //设置商品种类
        tPaymentRequest.dicRequest.put("PaymentType", dicRequest.getPaymentType());                             //设定支付类型
        tPaymentRequest.dicRequest.put("PaymentLinkType", dicRequest.getPaymentLinkType());                     //设定支付接入方式
        tPaymentRequest.dicRequest.put("NotifyType", dicRequest.getNotifyType());               //设定通知方式
        tPaymentRequest.dicRequest.put("ResultNotifyURL", dicRequest.getResultNotifyURL());     //设定通知URL地址
        tPaymentRequest.dicRequest.put("MerchantRemarks", dicRequest.getMerchantRemarks());     //设定附言
        tPaymentRequest.dicRequest.put("IsBreakAccount", dicRequest.getIsBreakAccount());       //设定交易是否分账、交易是否支持向二级商户入账
        tPaymentRequest.dicRequest.put("ChildMerchantNo", dicRequest.getChildMerchantNo());     //设定二级商户编号（大商户模式）

        //4、添加分账信息
        String[] SubMerchantID_arr = new String[]{};
        String[] SplitAmount_arr = new String[]{};

        if (SplitMerchantID != null && !SplitMerchantID.equals("")){
            SubMerchantID_arr = SplitMerchantID.split(",");
            SplitAmount_arr = SplitAmount.split(",");
        }

        LinkedHashMap map = null;

        if(SubMerchantID_arr.length > 0){
            for (int i = 0; i < SubMerchantID_arr.length; i++) {
                map = new LinkedHashMap();
                map.put("SplitMerchantID",SubMerchantID_arr[i]);
                map.put("SplitAmount",SplitAmount_arr[i]);

                tPaymentRequest.dicSplitAccInfo.put(i+1, map);
            }
        }

        JSON json = tPaymentRequest.postRequest();
        //JSON json = tPaymentRequest.extendPostRequest(1);

        Map<String,Object> backMap = new HashMap<>();

        String ReturnCode = json.GetKeyValue("ReturnCode");
        String ErrorMessage = json.GetKeyValue("ErrorMessage");
        String OneQRForAll = json.GetKeyValue("OneQRForAll");
        if (ReturnCode.equals("0000")) {
            System.out.println("ReturnCode   = [" + ReturnCode + "]<br/>");
            System.out.println("ErrorMessage = [" + ErrorMessage + "]<br/>");
            System.out.println("PaymentURL-->"+json.GetKeyValue("PaymentURL"));
            System.out.println("OneQRForAll = [" + OneQRForAll + "]<br/>");

            backMap.put("PaymentURL",json.GetKeyValue("PaymentURL"));
            backMap.put("OneQRForAll",OneQRForAll);
            backMap.put("state",true);
            backMap.put("ReturnCode",ReturnCode);
            //response.sendRedirect(json.GetKeyValue("PaymentURL"));
        }
        else {
            System.out.println("---------------支付出现异常Code:"+ ReturnCode +"---------------");
            System.out.println("---------------支付出现异常信息:"+ ErrorMessage +"---------------");
            backMap.put("state",false);
            backMap.put("ReturnCode",ReturnCode);
            backMap.put("ErrorMessage",ErrorMessage);
        }

        return XmlJsonMapUtil.getJSONFromMap(backMap);
    }


}
