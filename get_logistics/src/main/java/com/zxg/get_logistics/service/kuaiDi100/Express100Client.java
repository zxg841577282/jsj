package com.zxg.get_logistics.service.kuaiDi100;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zxg.get_logistics.web.req.Exp100SelectIM;
import okhttp3.*;
import org.springframework.stereotype.Service;
import other.ResultException;
import util.HttpClientUtil;
import util.MD5Util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/19 0019
 * @Purpose: 快递100实现类
 */
@Service
public class Express100Client implements Express100Service {



    @Override
    public JSONObject selectLogistics(Exp100SelectIM im) {
        Map<String,Object> info = new HashMap<>();

        info.put("com",im.getCom());
        info.put("num",im.getNum());
        if (im.getPhone() != null){ info.put("phone",im.getPhone()); }
        if (im.getFrom() != null){ info.put("from",im.getFrom());}
        if (im.getTo() != null){ info.put("to",im.getTo());}
        if (im.getResultv2() != null){ info.put("resultv2",im.getResultv2());}

        String param = JSON.toJSONString(info);
        //MD5加密
        String sign = MD5Util.getMD5(param + im.getKey() + im.getCustomer());
        Map<String,Object> params = new HashMap<>();
        params.put("param",param);
        params.put("sign",sign);
        params.put("customer",im.getCustomer());
        try {

            String url = "http://poll.kuaidi100.com/poll/query.do";

            JSONObject jsonObject = HttpClientUtil.doPost(String.valueOf(params),url);

            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultException("快递100查询物流轨迹失败");
        }
    }
}
