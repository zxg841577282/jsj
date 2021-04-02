package com.zxg.mina.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Author: zhou_xg
 * @Date: 2021/3/30
 * @Purpose:
 */

public class URLUtil {

    /**
     * 将URL编码
     */
    public static String encodeURL(String source,String coding){
        String target;
        try {
            target = URLEncoder.encode(source,coding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return target;
    }

    /**
     * 将URL解码
     */
    public static String dencodeURL(String source,String coding){
        String target;
        try {
            target = URLDecoder.decode(source,coding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return target;
    }

}
