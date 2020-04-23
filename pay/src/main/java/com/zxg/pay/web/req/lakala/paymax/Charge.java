package com.zxg.pay.web.req.lakala.paymax;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.zxg.pay.config.LklPaymaxConfig;
import com.zxg.pay.web.controller.lakala.paymax.other.Paymax;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaowei.wang on 2016/4/26.
 * 后续如果有新增字段，开发者只需要在此类中加上即可
 */
@Setter
@Getter
public class Charge extends Paymax {
    //支付订单id，系统内唯一，以“ch_”开头，后跟24位随机数
    private String id;
    //商户订单号，在商户系统内唯一，8-20位数字或字母，不允许特殊字符
    @JSONField(name="order_no")
    private String orderNo;
    //订单总金额，大于0的数字，单位是该币种的货币单位
    private BigDecimal amount;
    //购买商品的标题，最长32位
    private String subject;
    //购买商品的描述信息，最长128个字符
    private String body;
    //支付渠道编码，唯一标识一个支付渠道
    private String channel;
    //发起支付的客户端IP
    @JSONField(name="client_ip")
    private String clientIp;
    //	订单备注，限制300个字符内
    private String description;
    //	用户自定义元数据
    private Map<String, Object> metadata;
    //特定渠道需要的的额外附加参数
    private Map<String, Object> extra;
    //支付渠道订单号
    @JSONField(name="transaction_no")
    private String transactionNo;
    //应用的appKey
    private String app;
    //已结算金额
    @JSONField(name="amount_settle")
    private BigDecimal amountSettle;
    //三位ISO货币代码，只支持人民币cny，默认cny
    private String currency;
    //已退款金额
    @JSONField(name="amount_refunded")
    private BigDecimal amountRefunded;
    //是否已经开始退款
    private Boolean refunded;
    //退款记录
    private List<Refund> refunds;
    //订单创建时间，13位时间戳
    @JSONField(name="time_created")
    private Long timeCreated;
    //订单支付完成时间，13位时间戳
    @JSONField(name="time_paid")
    private Long timePaid;
    //	订单失效时间，13位时间戳
    @JSONField(name="time_expire")
    private Long timeExpire;
    //	订单结算时间，13位时间戳
    @JSONField(name="time_settle")
    private Long timeSettle;
    //支付凭据，用于调起支付APP或者跳转支付网关
    private Map<String, Object> credential;
    //订单的错误码
    @JSONField(name="failure_code")
    private String failureCode;
    //订单的错误消息的描述
    @JSONField(name="failure_msg")
    private String failureMsg;
    //是否是生产模式
    private Boolean liveMode;
    //订单状态，只有三种（PROCESSING-处理中，SUCCEED-成功，FAILED-失败）
    private String status;
    //本次请求是否成功 true:成功,false:失败
    private Boolean reqSuccessFlag;

    public Boolean getReqSuccessFlag() {
        return reqSuccessFlag;
    }

    /**
     * 创建充值订单
     * @param params
     */
    public static Charge create(Map<String,Object> params) throws IOException {
        return request(LklPaymaxConfig.API_BASE_URL+ LklPaymaxConfig.CREATE_CHARGE, JSONObject.toJSONString(params),Charge.class);
    }

    /**
     * 查询充值订单
     * @param chargeId
     */
    public static Charge retrieve(String chargeId) throws IOException {
        return request(LklPaymaxConfig.API_BASE_URL+ LklPaymaxConfig.CREATE_CHARGE+"/"+chargeId, null,Charge.class);
    }
}
