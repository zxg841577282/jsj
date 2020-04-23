package com.zxg.pay.web.controller.lakala.code.utils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author: zhou_xg
 * @Date: 2019/11/29 16:32
 * @Purpose:
 */

public class MapToXmlUtil {
    private static final String PREFIX_XML = "<?xml version=\"1.0\" encoding=\"GBK\" standalone=\"yes\"?><xml> ";
    private static final String SUFFIX_XML = "</xml>";

    public static String mapToXML(Map<String,Object> parm){
        //先排序
        List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(parm.entrySet());

        Collections.sort(infoIds, new Comparator<Map.Entry<String,  Object>>() {
            //升序排序
            public int compare(Map.Entry<String, Object> o1,
                               Map.Entry<String, Object> o2) {

                return o1.getKey().toLowerCase().compareTo(o2.getKey().toLowerCase());
            }
        });

        StringBuffer strbuff = new StringBuffer(PREFIX_XML);

        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, Object> stringStringEntry = infoIds.get(i);
            if (stringStringEntry.getKey().equals("wechatFees")){
                List<Map<String,String>> map = (List<Map<String,String>>) stringStringEntry.getValue();

                for (Map<String, String> stringStringMap : map) {

                    List<Map.Entry<String, String>> infoIds2 = new ArrayList<Map.Entry<String, String>>(stringStringMap.entrySet());

                    Collections.sort(infoIds2, new Comparator<Map.Entry<String,  String>>() {
                        //升序排序
                        public int compare(Map.Entry<String, String> o1,
                                           Map.Entry<String, String> o2) {

                            return o1.getKey().toLowerCase().compareTo(o2.getKey().toLowerCase());
                        }
                    });
                    strbuff.append("<").append(stringStringEntry.getKey()).append(">");
                    for (int i2 = 0; i2 < infoIds2.size(); i2++){
                        Map.Entry<String, String> stringStringEntry2 = infoIds2.get(i2);
                        strbuff.append("<").append(stringStringEntry2.getKey()).append(">");
                        strbuff.append(stringStringEntry2.getValue());
                        strbuff.append("</").append(stringStringEntry2.getKey()).append(">");
                    }
                    strbuff.append("</").append(stringStringEntry.getKey()).append(">");
                }
            }else {
                strbuff.append("<").append(stringStringEntry.getKey()).append(">");
                strbuff.append(stringStringEntry.getValue());
                strbuff.append("</").append(stringStringEntry.getKey()).append(">");
            }
        }
        return strbuff.append(SUFFIX_XML).toString();
    }

    public static Map<String,Object> convertToMap(Object obj){
        Map<String,Object> result=new HashMap<>();
        //获得类的的属性名 数组
        Field[]fields = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String name = new String(field.getName());
                result.put(name, field.get(obj));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String,Object> XMLToMap(String xml){
        try {
            Map map = new HashMap();
            Document document = DocumentHelper.parseText(xml);
            Element nodeElement = document.getRootElement();
            List node = nodeElement.elements();
            for (Iterator it = node.iterator(); it.hasNext();) {
                Element elm = (Element) it.next();
                map.put(elm.getName(), elm.getText());
                elm = null;
            }
            node = null;
            nodeElement = null;
            document = null;
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
