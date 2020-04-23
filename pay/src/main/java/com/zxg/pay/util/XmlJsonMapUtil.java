package com.zxg.pay.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import other.ResultException;

import javax.management.Attribute;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/21
 * @Purpose: XML Map JSON 相互转换
 */

public class XmlJsonMapUtil {

    /**
     * Xml转JSON
     * @param xml
     * @return
     */
    public static JSONObject XmlToJSONObject(String xml){
        try {
            Document document = DocumentHelper.parseText(xml);
            Element node = document.getRootElement();
            return ElementToJSONObject(node);
        }catch (DocumentException e){
            e.getStackTrace();
            throw new ResultException("XML格式转换异常");
        }
    }

    public static JSONObject ElementToJSONObject(Element node) {
            JSONObject result = new JSONObject();
            // 当前节点的名称、文本内容和属性
            List<Attribute> listAttr = node.attributes();// 当前节点的所有属性的list
            for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
                result.put(attr.getName(), attr.getValue());
            }
            // 递归遍历当前节点所有的子节点
            List<Element> listElement = node.elements();// 所有一级子节点的list
            if (!listElement.isEmpty()) {
                for (Element e : listElement) {// 遍历所有一级子节点
                    if (e.attributes().isEmpty() && e.elements().isEmpty()) // 判断一级节点是否有属性和子节点
                        result.put(e.getName(), e.getTextTrim());// 沒有则将当前节点作为上级节点的属性对待
                    else {
                        if (!result.containsKey(e.getName())) // 判断父节点是否存在该一级节点名称的属性
                            result.put(e.getName(), new JSONArray());// 没有则创建
                        ((JSONArray) result.get(e.getName())).add(ElementToJSONObject(e));// 将该一级节点放入该节点名称的属性对应的值中
                    }
                }
            }
            return result;
    }

    /**
     * map转XML格式String
     * @param map
     * @return
     */
    public static String getXMLFromMap(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        Iterator<String> mapIter = map.keySet().iterator();
        while (mapIter.hasNext()) {
            String key = mapIter.next();
            if ("".equals(map.get(key)) || map.get(key) == null) {
                continue;
            }

            if (!"sign".equals(key)) {
                sb.append("<" + key + "><![CDATA[" + map.get(key) + "]]></" + key + ">");
            } else {
                sb.append("<" + key + ">" + map.get(key) + "</" + key + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString().trim();
    }

    /**
     * MAP转JSON
     * @param map
     * @return
     */
    public static JSONObject getJSONFromMap(Map<String, Object> map){
        String json = JSON.toJSONString(map);//map转String
        return JSON.parseObject(json);//String转json
    }
}
