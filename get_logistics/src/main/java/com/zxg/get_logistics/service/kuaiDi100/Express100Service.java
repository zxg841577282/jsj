package com.zxg.get_logistics.service.kuaiDi100;

import com.alibaba.fastjson.JSONObject;
import com.zxg.get_logistics.web.req.Exp100SelectIM;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/19 0019
 * @Purpose:
 */

public interface Express100Service {
    //查询物流
    JSONObject selectLogistics(Exp100SelectIM im);
}
