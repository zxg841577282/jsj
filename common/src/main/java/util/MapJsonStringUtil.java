package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/8 0008
 * @Purpose: Map Json String List  相互转换
 */

public class MapJsonStringUtil {


    /**
     * Map转Json字符串
     */
    public static String MapToJsonString(Map map){
        return JSON.toJSONString(map);
    }

    /**
     * Json字符串转Map
     */
    public static Map<String, Object> JsonStringToMap(String msg){
        Map<String, Object> parse = (Map<String, Object>) JSON.parse(msg);
        return parse;
    }

    /**
     * String转Json
     */
    public static JSONObject StringToJson(String msg){
        return (JSONObject) JSONObject.parse(msg);
    }

    /**
     * List转Json字符串
     */
    public static String ListToJsonString(List<?> list){
        JSONArray ja = new JSONArray();
        ja.addAll(list);
        return ja.toString();
    }



}
