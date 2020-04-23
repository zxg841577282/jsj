package com.zxg.get_logistics.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zxg.get_logistics.service.kuaiDi100.Express100Service;
import com.zxg.get_logistics.web.req.Exp100SelectIM;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Author: zhou_xg
 * @Date: 2019/8/19 0019
 * @Purpose:
 */
@Api(description = "快递100-物流模板")
@RestController
@RequestMapping("/exp100")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Express100Controller {
    private final Express100Service express100Service;

    @ApiOperation(value = "查询物流")
    @PostMapping("/selectLogistics")
    public JSONObject selectLogistics(@RequestBody Exp100SelectIM im) {
        return express100Service.selectLogistics(im);
    }

    @ApiOperation(value = "获取物流公司列表")
    @PostMapping("/getComList")
    public Object getComList() {
        String s = "[\n" +
                "\t{\n" +
                "\t\t\"com\": \"zhongyouwuliu\",\n" +
                "\t\t\"company\": \"中邮物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"shunfeng\",\n" +
                "\t\t\"company\": \"顺丰速运\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"shentong\",\n" +
                "\t\t\"company\": \"申通快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"ems\",\n" +
                "\t\t\"company\": \"EMS快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"zhongtiewuliu\",\n" +
                "\t\t\"company\": \"中铁快运\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"zhaijisong\",\n" +
                "\t\t\"company\": \"宅急送\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"yuntongkuaidi\",\n" +
                "\t\t\"company\": \"运通快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"yuananda\",\n" +
                "\t\t\"company\": \"源安达\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"yunda\",\n" +
                "\t\t\"company\": \"韵达快运\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"yuefengwuliu\",\n" +
                "\t\t\"company\": \"越丰物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"yuanzhijiecheng\",\n" +
                "\t\t\"company\": \"元智捷诚快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"yuanweifeng\",\n" +
                "\t\t\"company\": \"源伟丰快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"yuantong\",\n" +
                "\t\t\"company\": \"圆通速递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"yuanchengwuliu\",\n" +
                "\t\t\"company\": \"远成物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"youshuwuliu\",\n" +
                "\t\t\"company\": \"优速物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"yibangwuliu\",\n" +
                "\t\t\"company\": \"一邦速递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"yafengsudi\",\n" +
                "\t\t\"company\": \"亚风速递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"xinhongyukuaidi\",\n" +
                "\t\t\"company\": \"鑫飞鸿物流快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"xingchengjibian\",\n" +
                "\t\t\"company\": \"星晨急便\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"xinfengwuliu\",\n" +
                "\t\t\"company\": \"信丰物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"xinbangwuliu\",\n" +
                "\t\t\"company\": \"新邦物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"wanxiangwuliu\",\n" +
                "\t\t\"company\": \"万象物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"wuyuansudi\",\n" +
                "\t\t\"company\": \"伍圆速递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"wenjiesudi\",\n" +
                "\t\t\"company\": \"文捷航空速递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"wanjiawuliu\",\n" +
                "\t\t\"company\": \"万家物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"ups\",\n" +
                "\t\t\"company\": \"UPS\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"tnt\",\n" +
                "\t\t\"company\": \"TNT\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"tiantian\",\n" +
                "\t\t\"company\": \"天天\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"tiandihuayu\",\n" +
                "\t\t\"company\": \"天地华宇\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"shengfengwuliu\",\n" +
                "\t\t\"company\": \"盛丰物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"suer\",\n" +
                "\t\t\"company\": \"速尔物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"shenghuiwuliu\",\n" +
                "\t\t\"company\": \"盛辉物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"quanyikuaidi\",\n" +
                "\t\t\"company\": \"全一快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"quanritongkuaidi\",\n" +
                "\t\t\"company\": \"全日通快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"quanjitong\",\n" +
                "\t\t\"company\": \"全际通物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"quanchenkuaidi\",\n" +
                "\t\t\"company\": \"全晨快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"peisihuoyunkuaidi\",\n" +
                "\t\t\"company\": \"配思货运\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"minghangkuaidi\",\n" +
                "\t\t\"company\": \"民航快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"longbanwuliu\",\n" +
                "\t\t\"company\": \"龙邦物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"lianhaowuliu\",\n" +
                "\t\t\"company\": \"联昊通物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"kuaijiesudi\",\n" +
                "\t\t\"company\": \"快捷速递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"jiayunmeiwuliu\",\n" +
                "\t\t\"company\": \"加运美\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"jiajiwuliu\",\n" +
                "\t\t\"company\": \"佳吉物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"jixianda\",\n" +
                "\t\t\"company\": \"急先达\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"jinguangsudikuaijian\",\n" +
                "\t\t\"company\": \"京广速递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"jiayiwuliu\",\n" +
                "\t\t\"company\": \"佳怡物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"huaxialongwuliu\",\n" +
                "\t\t\"company\": \"华夏龙物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"hengluwuliu\",\n" +
                "\t\t\"company\": \"恒路物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"huitongkuaidi\",\n" +
                "\t\t\"company\": \"汇通快运\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"guangdongyouzhengwuliu\",\n" +
                "\t\t\"company\": \"广东邮政物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"ganzhongnengda\",\n" +
                "\t\t\"company\": \"港中能达物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"fenghuangkuaidi\",\n" +
                "\t\t\"company\": \"凤凰快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"feikangda\",\n" +
                "\t\t\"company\": \"飞康达物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"fedex\",\n" +
                "\t\t\"company\": \"fedex\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"dsukuaidi\",\n" +
                "\t\t\"company\": \"D速快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"dhl\",\n" +
                "\t\t\"company\": \"DHL\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"dpex\",\n" +
                "\t\t\"company\": \"DPEX\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"debangwuliu\",\n" +
                "\t\t\"company\": \"德邦物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"datianwuliu\",\n" +
                "\t\t\"company\": \"大田物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"changyuwuliu\",\n" +
                "\t\t\"company\": \"长宇物流\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"coe\",\n" +
                "\t\t\"company\": \"中国东方\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"cces\",\n" +
                "\t\t\"company\": \"希伊艾斯快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"bht\",\n" +
                "\t\t\"company\": \"BHT\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"biaojikuaidi\",\n" +
                "\t\t\"company\": \"彪记快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"baifudongfang\",\n" +
                "\t\t\"company\": \"百福东方\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"anxindakuaixi\",\n" +
                "\t\t\"company\": \"安信达快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"anjiekuaidi\",\n" +
                "\t\t\"company\": \"安捷快递\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"com\": \"aae\",\n" +
                "\t\t\"company\": \"AAE全球专递\"\n" +
                "\t}\n" +
                "]\n";

        JSONArray jsonArray = JSONArray.parseArray(s);

        return jsonArray;
    }

}
