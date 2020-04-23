package com.zxg.get_logistics.service.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zxg.get_logistics.web.req.wx.*;
import org.springframework.stereotype.Service;
import util.AssertUtils;
import util.HttpClientUtil;
import util.MapJsonStringUtil;
import util.ValidationUtil;

import java.util.*;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/19 0019
 * @Purpose:
 */
@Service
public class WxExpressClient implements WxExpressService {

    //生成运单
    private static String WX_ADD_ORDER_URL = "https://api.weixin.qq.com/cgi-bin/express/business/order/add?access_token=ACCESS_TOKEN";

    //取消运单
    private static String WX_CANCEL_ORDER_URL = "https://api.weixin.qq.com/cgi-bin/express/business/order/cancel?access_token=ACCESS_TOKEN";

    //获取支持的快递公司列表
    private static String WX_Get_All_Delivery_URL = "https://api.weixin.qq.com/cgi-bin/express/business/delivery/getall?access_token=ACCESS_TOKEN";

    //获取运单数据
    private static String WX_Get_Order_URL = "https://api.weixin.qq.com/cgi-bin/express/business/order/get?access_token=ACCESS_TOKEN";

    //查询运单轨迹
    private static String WX_Get_Path_URL = "https://api.weixin.qq.com/cgi-bin/express/business/path/get?access_token=ACCESS_TOKEN";

    //获取电子面单余额。仅在使用加盟类快递公司时，才可以调用。
    private static String WX_Get_Quota_URL = "https://api.weixin.qq.com/cgi-bin/express/business/quota/get?access_token=ACCESS_TOKEN";

    @Override
    public String addOrder(WXAddOrderIM wxAddOrderIM, SenderOrReceiverIM senderIM, SenderOrReceiverIM receiverIM, ShopIM shopIM, CargoIM cargoIM, InsuredIM insuredIM, ServiceIM serviceIM) {
        //参数校验
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBeanList(Arrays.asList(wxAddOrderIM,senderIM,receiverIM,shopIM,cargoIM,insuredIM,serviceIM));
        AssertUtils.check(!validResult.hasErrors(),validResult.getErrors());

        String url = WX_ADD_ORDER_URL.replace("ACCESS_TOKEN", wxAddOrderIM.getAccess_token());

        Map<String,String> info = new HashMap<>();
        info.put("order_id",wxAddOrderIM.getOrder_id());
        info.put("openid",wxAddOrderIM.getOpenid());
        info.put("delivery_id",wxAddOrderIM.getDelivery_id());
        info.put("biz_id",wxAddOrderIM.getBiz_id());
        if (wxAddOrderIM.getCustom_remark() != null){ info.put("custom_remark",wxAddOrderIM.getCustom_remark()); }

        //寄件人sender
        Map<String,String> sender = new HashMap<>();
        sender.put("name",senderIM.getName());
        AssertUtils.isAllNull(senderIM.getTel() ,senderIM.getMobile(),"座机号码和手机号码必须有一个不为NULL");
        if (senderIM.getTel() != null){ sender.put("tel",senderIM.getTel()); }
        if (senderIM.getMobile() != null){ sender.put("mobile",senderIM.getMobile());}
        if (senderIM.getCompany() != null){ sender.put("company",senderIM.getCompany()); }
        if (senderIM.getPost_code() != null){ sender.put("post_code",senderIM.getPost_code()); }
        if (senderIM.getCountry() != null){ sender.put("country",senderIM.getCountry()); }
        if (senderIM.getProvince() != null){ sender.put("province",senderIM.getProvince()); }
        if (senderIM.getCity() != null){ sender.put("city",senderIM.getCity()); }
        if (senderIM.getArea() != null){ sender.put("area",senderIM.getArea()); }
        if (senderIM.getAddress() != null){ sender.put("address",senderIM.getAddress()); }
        info.put("sender", JSON.toJSONString(sender));

        //收件人receiver
        Map<String,String> receiver = new HashMap<>();
        receiver.put("name",receiverIM.getName());
        AssertUtils.isAllNull(senderIM.getTel() ,senderIM.getMobile(),"座机号码和手机号码必须有一个不为NULL");
        if (receiverIM.getTel() != null){ receiver.put("tel",receiverIM.getTel()); }
        if (receiverIM.getMobile() != null){ receiver.put("mobile",receiverIM.getMobile());}
        if (receiverIM.getCompany() != null){ receiver.put("company",receiverIM.getCompany()); }
        if (receiverIM.getPost_code() != null){ receiver.put("post_code",receiverIM.getPost_code()); }
        if (receiverIM.getCountry() != null){ receiver.put("country",receiverIM.getCountry()); }
        if (receiverIM.getProvince() != null){ receiver.put("province",receiverIM.getProvince()); }
        if (receiverIM.getCity() != null){ receiver.put("city",receiverIM.getCity()); }
        if (receiverIM.getArea() != null){ receiver.put("area",receiverIM.getArea()); }
        if (receiverIM.getAddress() != null){ receiver.put("address",receiverIM.getAddress()); }
        info.put("receiver", JSON.toJSONString(receiver));

        //商品信息，会展示到物流通知消息中shop
        Map<String,String> shop = new HashMap<>();
        shop.put("wxa_path",shopIM.getWxa_path());
        shop.put("img_url",shopIM.getImg_url());
        shop.put("goods_name",shopIM.getGoods_name());
        shop.put("goods_count", String.valueOf(shopIM.getGoods_count()));
        info.put("shop", JSON.toJSONString(shop));

        //包裹信息，将传递给快递公司cargo
        Map<String,String> cargo = new HashMap<>();
        cargo.put("count", String.valueOf(cargoIM.getCount()));
        cargo.put("weight",String.valueOf(cargoIM.getWeight()));
        cargo.put("space_x",String.valueOf(cargoIM.getSpace_x()));
        cargo.put("space_y",String.valueOf(cargoIM.getSpace_y()));
        cargo.put("space_z",String.valueOf(cargoIM.getSpace_z()));
        //包裹中商品详情列表detail_list
        List<Map<String,String>> detail_list = new ArrayList<>();
        for (CargoDetailListIM cargoDetailListIM : cargoIM.getDetailListIMList()) {
            Map<String,String> detail = new HashMap<>();
            detail.put("name",cargoDetailListIM.getName());
            detail.put("count", String.valueOf(cargoDetailListIM.getCount()));
            detail_list.add(detail);
        }
//        cargo.put("detail_list", JSONArray.parseObject(detail_list)JSONArray.fromObject(detail_list).toString());
        cargo.put("detail_list", JSONArray.toJSONString(detail_list));
        info.put("cargo", JSON.toJSONString(cargo));

        //保价信息insured
        Map<String,String> insured = new HashMap<>();
        insured.put("use_insured", String.valueOf(insuredIM.getUse_insured()));
        insured.put("insured_value", String.valueOf(insuredIM.getInsured_value()));
        info.put("insured", JSON.toJSONString(insured));

        //服务类型service
        Map<String,String> service = new HashMap<>();
        service.put("service_type", String.valueOf(serviceIM.getService_type()));
        service.put("service_name",serviceIM.getService_name());
        info.put("service", JSON.toJSONString(service));

        //访问
        JSONObject jsonObject = HttpClientUtil.doPost(JSON.toJSONString(info),url);
        String errcode = String.valueOf(jsonObject.get("errcode"));
        //返回信息back
        Map<String,String> back = new HashMap<>();
        if (!errcode.isEmpty()){
            back.put("errcode",errcode);
            back.put("errmsg",String.valueOf(jsonObject.get("errmsg")));
            back.put("delivery_resultcode",String.valueOf(jsonObject.get("delivery_resultcode")));
            back.put("delivery_resultmsg",String.valueOf(jsonObject.get("delivery_resultmsg")));
        }else {
            back.put("order_id",String.valueOf(jsonObject.get("order_id")));
            back.put("waybill_id",String.valueOf(jsonObject.get("waybill_id")));
        }

        return MapJsonStringUtil.MapToJsonString(back);
    }

    @Override
    public String cancelOrder(CancelOrderIM cancelOrderIM) {
        //参数校验
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(cancelOrderIM);
        AssertUtils.check(!validResult.hasErrors(),validResult.getErrors());

        String url = WX_CANCEL_ORDER_URL.replace("ACCESS_TOKEN", cancelOrderIM.getAccess_token());

        Map<String,String> info = new HashMap<>();
        info.put("order_id",cancelOrderIM.getOrder_id());
        if (cancelOrderIM.getOpenid() != null){ info.put("openid",cancelOrderIM.getOpenid()); }
        info.put("delivery_id",cancelOrderIM.getDelivery_id());
        info.put("waybill_id",cancelOrderIM.getWaybill_id());

        JSONObject jsonObject = HttpClientUtil.doPost(JSON.toJSONString(info),url);
        //返回信息back
        Map<String,String> back = new HashMap<>();
        back.put("errcode",String.valueOf(jsonObject.get("errcode")));
        back.put("errmsg",String.valueOf(jsonObject.get("errmsg")));

        return MapJsonStringUtil.MapToJsonString(back);
    }

    @Override
    public String getAllDelivery(String access_token) {
        AssertUtils.isNull(access_token,"接口调用凭证不能为NULL");
        String url = WX_Get_All_Delivery_URL.replace("ACCESS_TOKEN", access_token);
        JSONObject jsonObject = HttpClientUtil.doGet(url);
        String data = jsonObject.getString("data");
        Map<String,String> back = new HashMap<>();
        back.put("data",data);
        return MapJsonStringUtil.MapToJsonString(back);
    }

    @Override
    public String getOrder(GetOrderIM getOrderIM) {
        //参数校验
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(getOrderIM);
        AssertUtils.check(!validResult.hasErrors(),validResult.getErrors());

        String url = WX_Get_Order_URL.replace("ACCESS_TOKEN", getOrderIM.getAccess_token());

        Map<String,String> info = new HashMap<>();
        info.put("order_id",getOrderIM.getOrder_id());
        if (getOrderIM.getOpenid() != null){ info.put("openid",getOrderIM.getOpenid()); }
        info.put("delivery_id",getOrderIM.getDelivery_id());
        info.put("waybill_id",getOrderIM.getWaybill_id());

        JSONObject jsonObject = HttpClientUtil.doPost(JSON.toJSONString(info),url);
        //返回信息back
        Map<String,String> back = new HashMap<>();
        back.put("waybill_data",String.valueOf(jsonObject.get("waybill_data")));
        back.put("print_html",String.valueOf(jsonObject.get("print_html")));

        return MapJsonStringUtil.MapToJsonString(back);
    }

    @Override
    public String getPath(GetPathIM getPathIM) {
        //参数校验
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(getPathIM);
        AssertUtils.check(!validResult.hasErrors(),validResult.getErrors());

        String url = WX_Get_Path_URL.replace("ACCESS_TOKEN", getPathIM.getAccess_token());

        Map<String,String> info = new HashMap<>();
        info.put("order_id",getPathIM.getOrder_id());
        if (getPathIM.getOpenid() != null){ info.put("openid",getPathIM.getOpenid()); }
        info.put("delivery_id",getPathIM.getDelivery_id());
        info.put("waybill_id",getPathIM.getWaybill_id());

        JSONObject jsonObject = HttpClientUtil.doPost(JSON.toJSONString(info),url);
        //返回信息back
        Map<String,String> back = new HashMap<>();
        back.put("waybill_id",String.valueOf(jsonObject.get("waybill_id")));
        back.put("delivery_id",String.valueOf(jsonObject.get("delivery_id")));
        back.put("path_item_num",String.valueOf(jsonObject.get("path_item_num")));
        back.put("path_item_list",String.valueOf(jsonObject.get("path_item_list")));

        return MapJsonStringUtil.MapToJsonString(back);
    }

    @Override
    public String getQuota(String access_token,String biz_id) {
        AssertUtils.isNull(access_token,"接口调用凭证不能为NULL");
        AssertUtils.isNull(biz_id,"快递客户编码或者现付编码不能为NULL");
        String url = WX_Get_Quota_URL.replace("ACCESS_TOKEN", access_token);

        Map<String,String> info = new HashMap<>();
        info.put("delivery_id","ZTO");
        info.put("biz_id",biz_id);

        JSONObject jsonObject = HttpClientUtil.doPost(JSON.toJSONString(info),url);
        //返回信息back
        Map<String,String> back = new HashMap<>();
        back.put("delivery_id",String.valueOf(jsonObject.get("delivery_id")));
        back.put("biz_id",String.valueOf(jsonObject.get("biz_id")));
        back.put("quota_num",String.valueOf(jsonObject.get("quota_num")));

        return MapJsonStringUtil.MapToJsonString(back);
    }
}
